# Guía del Estudiante — Clase 08: Arquitectura MVVM
## Programación para Dispositivos Móviles — Universidad de Caldas
**Facultad de Ingeniería** · Unidad 3: Arquitectura y Datos · Semana 8 · Duración: 3 horas

---

## ¿Qué voy a aprender?

Hasta ahora tu código funciona. Hoy aprendes a escribir código que **escala**:

1. Por qué escribir toda la lógica dentro de un Composable o una Activity se vuelve difícil de mantener y de testear.
2. Qué es el patrón MVVM (Model - View - ViewModel) y qué responsabilidad tiene cada capa.
3. Cómo usar `ViewModel` y `StateFlow` para separar la lógica de negocio de la interfaz.
4. Cómo el `ViewModel` sobrevive a cambios de configuración (como rotar la pantalla) sin perder su estado.

**Por qué importa:** esta es la arquitectura que usarás en el proyecto final. Cada una de las 5 rutas tendrá su propio ViewModel.

---

## Antes de la clase

- Trae funcionando la app de lista + detalle de la Clase 6.
- Repasa qué es `remember`/`mutableStateOf` (Clase 5): MVVM resuelve el mismo problema pero a mayor escala.

### Autochequeo de entrada
1. ¿Qué pasa con el estado guardado en `remember` cuando rotas la pantalla? (Clase 4/5)
2. ¿Qué diferencia hay entre `remember` y `rememberSaveable`? (Clase 5)
3. ¿Qué es una `data class`? (Clase 3)

---

## Durante la clase

### Bloque 1 — El problema y las tres capas

#### 1.1 El anti-patrón: todo en el Composable

```kotlin
// ANTES: lógica y UI mezcladas (funciona, pero no escala)
@Composable
fun ListaCursosScreen() {
    var cursos by remember { mutableStateOf(listOf<Curso>()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        cargando = true
        cursos = obtenerCursosDesdeAlgunLado() // ¿de dónde? ¿cómo se testea esto?
        cargando = false
    }
    // ... UI que usa 'cursos' y 'cargando'
}
```

**Los tres problemas concretos:**

| Problema | Por qué duele |
|---|---|
| **No se puede testear** | Para probar la carga de datos necesitas un emulador. |
| **No sobrevive a la rotación** | `remember` se pierde cuando la Activity se recrea. |
| **No escala** | Si la lógica crece, el Composable se vuelve inmanejable. |

#### 1.2 Las tres capas

```
┌─────────────────────────────────────────┐
│  VIEW (Composables)                     │
│  - Muestra estado                       │
│  - Emite eventos del usuario            │
│  - NO sabe de dónde vienen los datos    │
└──────────────┬──────────────────────────┘
               │ observa estado ↑
               │ emite eventos ↓
┌──────────────┴──────────────────────────┐
│  VIEWMODEL                              │
│  - Expone estado (StateFlow)            │
│  - Ejecuta la lógica de presentación    │
│  - Sobrevive a cambios de configuración │
└──────────────┬──────────────────────────┘
               │ pide datos ↓
┌──────────────┴──────────────────────────┐
│  MODEL (datos / repositorio)            │
│  - data class                           │
│  - Fuente de datos (red, BD, memoria)   │
└─────────────────────────────────────────┘
```

| Capa | Responsabilidad | Qué NO debe hacer |
|---|---|---|
| **Model** | Datos y lógica de negocio / acceso a datos | No conoce la UI |
| **View** | Mostrar estado y capturar eventos | No contiene lógica de negocio |
| **ViewModel** | Exponer estado y ejecutar lógica de presentación | No conoce widgets ni Composables |

**Punto clave:** el `ViewModel` **no conoce Compose**. No importa `androidx.compose.*`. Si escribes `Text(...)` dentro de un `ViewModel`, algo está mal.

---

### Bloque 2 — El ViewModel con StateFlow

#### 2.1 Las dependencias

```kotlin
// build.gradle.kts (Module :app)
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
```

La segunda trae `collectAsStateWithLifecycle`. Sin ella, obtendrás `Unresolved reference`.

#### 2.2 El ViewModel completo

```kotlin
class CursosViewModel : ViewModel() {

    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    init {
        cargarCursos()
    }

    fun cargarCursos() {
        viewModelScope.launch {
            _cargando.value = true
            _cursos.value = listOf(
                Curso(1, "Programación Móvil", 3),
                Curso(2, "Bases de Datos", 4),
                Curso(3, "Redes", 3)
            )
            _cargando.value = false
        }
    }
}
```

**Los cinco conceptos:**

1. **`ViewModel`** — clase base de Android que sobrevive a los cambios de configuración.
2. **`MutableStateFlow`** — flujo observable **mutable**. Es privado (`_cursos`).
3. **`StateFlow`** — la versión de **solo lectura**. Es pública (`cursos`). **Este es el patrón de encapsulamiento.**
4. **`viewModelScope`** — el alcance de corrutinas del ViewModel. Se cancela automáticamente cuando el ViewModel muere. **Evita fugas de memoria.**
5. **`init { }`** — bloque que se ejecuta al crear el ViewModel. Aquí se dispara la carga inicial.

**¿Por qué `_cursos` es privado y `cursos` es público?** Para que nadie fuera del ViewModel pueda modificar el estado. La View solo puede **leer**; para cambiar algo, debe llamar una función pública.

**Advertencia:** si usas `GlobalScope` en lugar de `viewModelScope`, la corrutina sigue viva aunque el ViewModel muera → fuga de memoria.

#### 2.3 La View

```kotlin
@Composable
fun ListaCursosScreen(viewModel: CursosViewModel = viewModel()) {
    val cursos by viewModel.cursos.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()

    if (cargando) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(cursos, key = { it.id }) { curso -> Text(text = curso.nombre) }
        }
    }
}
```

**Los tres detalles:**

1. **`viewModel()`** — obtiene (o crea) el ViewModel asociado a esta pantalla. **Nunca escribas `CursosViewModel()` directamente**: eso crea una instancia nueva en cada recomposición y rompe la supervivencia a la rotación.
2. **`collectAsStateWithLifecycle()`** — convierte el `StateFlow` en un `State` de Compose, respetando el ciclo de vida. **No uses `collectAsState()`** a secas: sigue colectando aunque la pantalla esté en segundo plano.
3. **`by`** — delegación de propiedades. Se lee mejor que `.value`.

---

### Playground 1 — Mueve el estado a un ViewModel (15 min)

**Tu turno.** Toma la pantalla de lista de la Clase 6 y mueve la lista de datos al `ViewModel`.

1. Crea `MiViewModel : ViewModel()` con `_elementos`/`elementos` y `_cargando`/`cargando`.
2. Mueve la lista hardcodeada al `init { }`.
3. Conecta el Composable con `viewModel()` y `collectAsStateWithLifecycle()`.

**Checkpoint 1:** la lista se muestra igual que antes, pero ahora vive en el ViewModel. Verifícalo rotando: la lista no se recarga.

**Errores frecuentes:**
- Escribir `MiViewModel()` en lugar de `viewModel()`.
- Exponer `MutableStateFlow` en lugar de `StateFlow`.
- Olvidar la dependencia `lifecycle-runtime-compose`.

---

### Bloque 3 — Eventos de la UI hacia el ViewModel

#### 3.1 El flujo unidireccional

```
        ┌──────────────┐
        │   ViewModel  │
        │  _cursos     │
        └──────┬───────┘
               │ StateFlow (estado)
               ↓
        ┌──────────────┐
        │     View     │
        │  Checkbox    │
        └──────┬───────┘
               │ evento (función pública)
               ↓
        ┌──────────────┐
        │   ViewModel  │
        │ marcarFavorito│
        └──────────────┘
```

**Regla de oro:** el estado **baja** (View observa), los eventos **suben** (View llama funciones). Nunca al revés.

#### 3.2 El código

```kotlin
class CursosViewModel : ViewModel() {
    // ...
    fun marcarFavorito(cursoId: Int) {
        _cursos.value = _cursos.value.map {
            if (it.id == cursoId) it.copy(esFavorito = !it.esFavorito) else it
        }
    }
}

// En la View:
Checkbox(
    checked = curso.esFavorito,
    onCheckedChange = { viewModel.marcarFavorito(curso.id) }
)
```

**Detalle importante:** `_cursos.value = _cursos.value.map { ... }` — se **reemplaza** la lista completa con una nueva. No se muta el elemento. Esto es coherente con la inmutabilidad de `data class` de la Clase 3, y es lo que permite que Compose detecte el cambio.

#### 3.3 El bug que vas a ver en la demo

```kotlin
class CursosViewModel : ViewModel() {
    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos

    // BUG: esta copia local se actualiza, pero _cursos.value nunca cambia
    private var cursosCache: List<Curso> = emptyList()

    fun marcarFavorito(id: Int) {
        cursosCache = cursosCache.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
        // Falta: _cursos.value = cursosCache
    }
}
```

La app no falla, no hay error en Logcat, el código "se ejecuta"... y **la UI no cambia**.

**¿Por qué?** Porque Compose observa el `StateFlow`, no las variables arbitrarias de la clase. `cursosCache` es invisible para Compose.

**La corrección:**

```kotlin
fun marcarFavorito(id: Int) {
    _cursos.value = _cursos.value.map {
        if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
    }
}
```

**Lección de diseño:** **una sola fuente de verdad**. Si tienes el estado en dos lugares, tarde o temprano se desincronizan.

---

### Playground 2 — Refactoriza lista + detalle (25 min)

**Tu turno.** Refactoriza la app completa de la Clase 6 (lista + detalle + búsqueda) a MVVM.

1. El `ViewModel` expone la lista completa y el texto de búsqueda.
2. El filtrado se hace **dentro del ViewModel** (no en el Composable).
3. La pantalla de detalle obtiene su elemento del mismo `ViewModel`, filtrando por id.
4. La acción de "marcar favorito" es una función pública del `ViewModel`.

**Checkpoint 2:** la búsqueda sigue funcionando, la navegación sigue funcionando, y al rotar en el detalle **no se pierde el estado**.

**Errores frecuentes:**
- Dejar el filtrado en el Composable (mezcla de responsabilidades).
- Crear dos ViewModels distintos para lista y detalle (pierden el estado compartido).

---

### Bloque 4 — ¿Por qué el ViewModel sobrevive a la rotación?

Android gestiona internamente el `ViewModel` asociado a una Activity/pantalla a través de un **`ViewModelStore`**, que persiste mientras la Activity no se destruya definitivamente. Al rotar:

- La **Activity** se destruye y se recrea (Clase 4).
- El **ViewModel** obtenido con `viewModel()` es **el mismo objeto**.

| Mecanismo | Sobrevive a rotación | Sobrevive a muerte de proceso | Sobrevive a cierre con "Atrás" |
|---|---|---|---|
| `remember` | ❌ | ❌ | ❌ |
| `rememberSaveable` | ✅ | ✅ | ❌ |
| `onSaveInstanceState` | ✅ | ✅ | ❌ |
| `ViewModel` | ✅ | ❌ | ❌ |
| Base de datos (Clase 10) | ✅ | ✅ | ✅ |

**Punto clave:** el `ViewModel` **no** sobrevive a la muerte del proceso. Si el sistema mata la app en segundo plano, el ViewModel se va. Para eso están `SavedStateHandle` y, sobre todo, la persistencia real (Clase 10).

---

## Después de la clase (trabajo independiente — ~6 horas)

Completa el **`Taller_Clase08.md`** (Taller 6 — Reto: MVVM Real con un Bug de Estado que Debes Cazar).

### Distribución sugerida de las 6 horas

| Parte | Contenido | Tiempo |
|---|---|---|
| Predicción | ¿La UI refleja el cambio si actualizas solo la variable local? | 10 min |
| Parte 1 | Extraer el ViewModel | 2 h |
| Parte 2 | Cacería de bugs: el estado que se actualiza pero no se ve | 2 h |
| Parte 3 | Verificación de supervivencia a la rotación | 1.5 h |
| Parte 4 | Bonus: estado derivado / manejo de error / test unitario | opcional |

**Observa el peso de la rúbrica:** la cacería de bugs (30) vale más que la implementación (25). El taller evalúa si **entiendes el flujo unidireccional**, no solo si lograste que compilara.

---

## Conceptos clave para repasar

| Concepto | En una frase |
|---|---|
| Model | Los datos y la lógica de negocio (o el acceso a datos). |
| View | La UI (Composables); solo muestra estado y envía eventos del usuario. |
| ViewModel | Intermediario: expone estado a la View y ejecuta la lógica que responde a sus eventos. |
| `StateFlow` | Flujo observable de un solo valor actual; la View lo colecta y se recompone al cambiar. |
| `MutableStateFlow` | Versión mutable del `StateFlow`; debe ser privada. |
| `viewModelScope` | Alcance de corrutinas del ViewModel; se cancela al morir el ViewModel. |
| `collectAsStateWithLifecycle()` | Convierte un `StateFlow` en `State` de Compose respetando el ciclo de vida. |
| `viewModel()` | Obtiene o crea el ViewModel asociado a la pantalla. |
| Flujo unidireccional | El estado baja, los eventos suben. |
| Fuente única de verdad | El estado vive en un solo lugar: el `StateFlow` del ViewModel. |
| Separación de responsabilidades | Cada capa hace una sola cosa; facilita testing y mantenimiento. |

---

## Autoevaluación

Responde sin mirar. Si fallas 3 o más, vuelve al bloque correspondiente.

1. ¿Cuáles son los tres problemas de poner la lógica dentro del Composable?
2. ¿Qué responsabilidad tiene cada una de las tres capas de MVVM?
3. ¿Por qué `_cursos` es privado y `cursos` es público?
4. ¿Qué diferencia hay entre `MutableStateFlow` y `StateFlow`?
5. ¿Por qué se usa `viewModelScope` y no `GlobalScope`?
6. ¿Qué pasa si escribes `CursosViewModel()` en lugar de `viewModel()`?
7. ¿Por qué `collectAsStateWithLifecycle()` y no `collectAsState()`?
8. ¿Por qué la UI no cambia si actualizas una variable local en lugar del `StateFlow`?
9. ¿A qué sobrevive el ViewModel y a qué no?
10. ¿Por qué el ViewModel no debe importar clases de Compose?

<details>
<summary>Respuestas</summary>

1. No se puede testear, no sobrevive a la rotación, no escala.
2. Model: datos/negocio. View: mostrar estado y capturar eventos. ViewModel: exponer estado y ejecutar lógica de presentación.
3. Para encapsular: la View solo lee; para cambiar el estado debe llamar una función pública.
4. `MutableStateFlow` permite modificar el valor; `StateFlow` es de solo lectura.
5. Porque `viewModelScope` se cancela automáticamente al morir el ViewModel; `GlobalScope` no, y genera fugas de memoria.
6. Se crea una instancia nueva en cada recomposición y se pierde la supervivencia a la rotación.
7. Porque respeta el ciclo de vida: deja de colectar cuando la pantalla está en segundo plano.
8. Porque Compose observa el `StateFlow`, no las variables arbitrarias de la clase.
9. Sobrevive a cambios de configuración (rotación, idioma, tema). No sobrevive a la muerte del proceso ni al cierre con "Atrás".
10. Porque el ViewModel no debe conocer la UI: eso rompe la separación de capas y lo hace imposible de testear sin Android.
</details>

---

## Solución de problemas (troubleshooting)

| Síntoma | Causa probable | Solución |
|---|---|---|
| `Unresolved reference: collectAsStateWithLifecycle` | Falta la dependencia | Agrega `lifecycle-runtime-compose` |
| `Unresolved reference: viewModelScope` | Falta la dependencia | Agrega `lifecycle-viewmodel-ktx` |
| `Unresolved reference: viewModel` | Falta la dependencia | Agrega `lifecycle-viewmodel-compose` |
| La UI no se actualiza al cambiar el estado | Hay una variable duplicada que no es el `StateFlow` | Una sola fuente de verdad: actualiza `_cursos.value` |
| El estado se pierde al rotar | El estado quedó en `remember` | Muévelo al ViewModel |
| El ViewModel se recrea al rotar | Usaste `MiViewModel()` en lugar de `viewModel()` | Usa `viewModel()` |
| Fuga de memoria | Usaste `GlobalScope` | Usa `viewModelScope` |
| El ViewModel importa clases de Compose | Mezcla de capas | El ViewModel no conoce la UI |
| La lista parpadea al rotar | El `init` vuelve a cargar | Verifica que el ViewModel no se esté recreando |

---

## Glosario

- **MVVM:** Model - View - ViewModel; patrón de arquitectura que separa datos, UI y lógica de presentación.
- **ViewModel:** componente que expone estado a la UI y sobrevive a los cambios de configuración.
- **StateFlow:** flujo observable que siempre tiene un valor actual; emite el valor nuevo a cada colector.
- **MutableStateFlow:** versión mutable del `StateFlow`; debe mantenerse privada.
- **Colector:** quien observa un flujo y reacciona a sus emisiones.
- **`viewModelScope`:** alcance de corrutinas ligado al ciclo de vida del ViewModel.
- **Flujo unidireccional de datos:** patrón donde el estado baja y los eventos suben.
- **Fuente única de verdad:** principio de que el estado debe vivir en un solo lugar.
- **`ViewModelStore`:** estructura interna de Android que conserva los ViewModels durante los cambios de configuración.
- **Encapsulamiento:** ocultar el estado mutable y exponer solo una vista de solo lectura.

---

## Enlace con la siguiente clase

La **Clase 9** conecta el `ViewModel` con una API real usando **Retrofit y corrutinas**. El ViewModel no va a cambiar de forma: solo cambiará de dónde vienen los datos.

- Hoy: `_cursos.value = listOf(Curso(1, ...), ...)` (hardcodeado).
- Clase 9: `_cursos.value = api.obtenerCursos()` (desde internet).

Y en el **proyecto final** (5 APIs, ver `_Material de Curso (Programa)/Proyecto_Final_5_APIs.md`): cada una de las 5 rutas tendrá **un ViewModel propio** que expondrá su estado con `StateFlow` y usará un `sealed class` (Clase 3) para representar cargando/éxito/error. Los tres conceptos de las Clases 3, 6 y 8 convergen aquí.
