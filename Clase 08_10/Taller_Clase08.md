# Taller 6 — Reto: MVVM Real (con un Bug de Estado que Debes Cazar)
## Programación para Dispositivos Móviles — Universidad de Caldas
**Facultad de Ingeniería** · Unidad 3: Arquitectura y Datos
**Modalidad:** individual · **Nivel:** exigente

> **Créditos y tiempo de trabajo independiente:** el curso tiene 3 créditos. Bajo la norma de la Universidad de Caldas (1 crédito ≈ 48 horas de trabajo académico/semestre, en proporción aproximada 1:2 entre horas de acompañamiento directo y horas de trabajo autónomo), cada semana implica ~3 horas de clase presencial y **~6 horas de trabajo independiente**. Este taller está diseñado para consumir esas 6 horas fuera de clase (o completarse parcialmente en el bloque práctico de la sesión).

---

## Por qué este taller es más difícil de lo que parece

Casi cualquiera puede crear una clase llamada `ViewModel`. El reto real es no romper el flujo unidireccional de datos por accidente, algo que ocurre constantemente en proyectos reales cuando se "cachea" el estado en una variable separada del `StateFlow` oficial.

---

## Cómo trabajar este taller

Cada parte tiene **micro-pasos numerados**. Para cada paso encontrarás:

- **Qué hacer** — la instrucción.
- **El código** — lo que debes escribir (o el punto de partida).
- **Qué deberías ver** — la evidencia esperada en pantalla o en Logcat.
- **Checkpoint** — el criterio para saber que puedes avanzar.

**Regla:** no avances al siguiente paso si el checkpoint no se cumple.

---

## Antes de empezar: predicción (obligatorio)

Escribe tu predicción **antes** de programar:

> Si tu `ViewModel` guarda la lista en una variable normal (`var cursosCache = listOf(...)`) además de en el `MutableStateFlow`, y actualizas solo la variable pero no el `StateFlow`, ¿la UI reflejará el cambio? Justifica tu respuesta.

La comprobarás en la Parte 2.

---

## Requisitos funcionales

1. Un `ViewModel` que expone la lista de elementos como `StateFlow<List<T>>` (ya no hardcodeada en el Composable).
2. Un estado de carga (`StateFlow<Boolean>`) que se muestre como `CircularProgressIndicator` mientras "carga" (simula una espera de 1 segundo con `delay`).
3. Al menos una acción del usuario (marcar favorito, calificar, etc.) implementada como una función pública del `ViewModel`, nunca modificando el estado directamente desde la View.
4. La pantalla de detalle también debe observar su información desde el `ViewModel` (puede ser el mismo `ViewModel` de la lista, filtrando por id).

---

## Parte 1 — Extraer el ViewModel (2 horas aprox.)

### Paso 1.1 — Agrega las dependencias
**Qué hacer:** en `app/build.gradle.kts`:

```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
```

Sincroniza Gradle.

**Checkpoint 1.1:** el IDE reconoce `viewModel()` y `collectAsStateWithLifecycle` sin errores.

### Paso 1.2 — Crea la clase del ViewModel
**Qué hacer:** crea `TuTemaViewModel : ViewModel()` con los dos flujos.

```kotlin
class TuTemaViewModel : ViewModel() {

    private val _elementos = MutableStateFlow<List<Elemento>>(emptyList())
    val elementos: StateFlow<List<Elemento>> = _elementos

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    init {
        cargarElementos()
    }

    fun cargarElementos() {
        viewModelScope.launch {
            _cargando.value = true
            delay(1000)   // simula una espera de red
            _elementos.value = listOf(/* tus 12+ elementos */)
            _cargando.value = false
        }
    }
}
```

**Checkpoint 1.2:** `_elementos` es privado y `elementos` es público de tipo `StateFlow`.

### Paso 1.3 — Mueve la lista de datos
**Qué hacer:** elimina la lista hardcodeada del Composable y muévela al `init`/`cargarElementos` del ViewModel.

**Checkpoint 1.3:** el Composable ya no contiene la lista de datos.

### Paso 1.4 — Conecta la pantalla de lista
**Qué hacer:** observa el ViewModel desde el Composable.

```kotlin
@Composable
fun ListaScreen(viewModel: TuTemaViewModel = viewModel()) {
    val elementos by viewModel.elementos.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()

    if (cargando) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(elementos, key = { it.id }) { elemento -> /* Card ... */ }
        }
    }
}
```

**Qué deberías ver:** al abrir la app, un `CircularProgressIndicator` durante ~1 segundo, y luego la lista.

**Checkpoint 1.4 (final de la Parte 1):** la lista se muestra y el indicador de carga aparece y desaparece.

---

## Parte 2 — Cacería de bugs: el estado que se actualiza pero no se ve (2 horas aprox.)

### Paso 2.1 — Implementa la versión con bug
**Qué hacer:** implementa **a propósito** esta versión de `marcarFavorito` y observa qué pasa.

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

**Qué deberías ver:** la app **no falla**, no hay error en Logcat, el código "se ejecuta sin errores"... y la UI **no cambia**.

**Checkpoint 2.1:** confirmaste que la UI no cambia y tienes evidencia (captura o descripción).

### Paso 2.2 — Diagnostica
**Qué hacer:** explica por escrito por qué Compose no puede detectar el cambio en `cursosCache` aunque sea una propiedad de la misma clase.

**Checkpoint 2.2:** tu explicación menciona que Compose observa el `StateFlow`, no las variables arbitrarias de la clase.

### Paso 2.3 — Corrige
**Qué hacer:** elimina la variable duplicada y actualiza `_cursos.value` directamente con el resultado del `.map { ... }`.

```kotlin
fun marcarFavorito(id: Int) {
    _cursos.value = _cursos.value.map {
        if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
    }
}
```

**Qué deberías ver:** al marcar un favorito, la UI se actualiza inmediatamente.

**Checkpoint 2.3 (final de la Parte 2):** la UI refleja el cambio y no queda ninguna variable de estado duplicada.

### Paso 2.4 — Verifica que no quedan duplicados
**Qué hacer:** busca en tu ViewModel cualquier `var` que guarde estado que también esté en un `StateFlow`. Elimínalos todos.

**Checkpoint 2.4:** el ViewModel tiene **una sola fuente de verdad** por cada pieza de estado.

---

## Parte 3 — Verificación de supervivencia a la rotación (1.5 horas aprox.)

### Paso 3.1 — Marca favoritos
**Qué hacer:** marca 2 o 3 elementos como favoritos.

**Checkpoint 3.1:** los favoritos se ven marcados en la UI.

### Paso 3.2 — Rota la pantalla
**Qué hacer:** rota el dispositivo y verifica que los favoritos **NO** se pierden.

**Qué deberías ver:** los favoritos siguen marcados después de la rotación.

**Checkpoint 3.2:** los favoritos sobreviven a la rotación.

### Paso 3.3 — Prueba el contraste
**Qué hacer:** mueve temporalmente el estado a `remember` dentro del Composable, rota, y observa la diferencia. Luego devuélvelo al ViewModel.

**Checkpoint 3.3:** comprobaste empíricamente la diferencia entre `remember` y `ViewModel`.

### Paso 3.4 — Documenta
**Qué hacer:** en `ANALISIS_MVVM.md`, explica qué hubiera pasado si el estado hubiera quedado en `remember` dentro del Composable en lugar del `ViewModel`.

**Checkpoint 3.4 (final de la Parte 3):** la reflexión está escrita y es específica (no genérica).

---

## Parte 4 — Reto avanzado (bonus, +15 puntos, elige uno)

- **Opción A:** implementa un segundo estado derivado (`StateFlow`) que cuente cuántos elementos son favoritos, calculado con `.map` sobre el `StateFlow` principal (sin duplicar el conteo manualmente en otra variable).
- **Opción B:** agrega manejo de error simulado (por ejemplo, una probabilidad de "falla" al cargar) representado con un tercer estado (`StateFlow<String?>` para el mensaje de error) mostrado en la UI.
- **Opción C:** escribe una prueba unitaria simple (sin Android, JUnit puro) que verifique que `marcarFavorito` cambia correctamente el estado del `ViewModel`.

---

## Entregables

- Código fuente completo (repositorio Git o `.zip`).
- Archivo `ANALISIS_MVVM.md` con:
  - la predicción inicial (y si acertaste),
  - la explicación del bug de la Parte 2,
  - la reflexión de la Parte 3.

---

## Rúbrica

| Criterio | Puntos |
|---|---|
| ViewModel con `StateFlow` correctamente implementado (sin variables duplicadas) | 25 |
| Estado de carga funcional | 10 |
| Cacería de bugs: diagnóstico y corrección justificada del estado que no se actualizaba | 30 |
| Verificación de supervivencia a rotación + análisis escrito | 20 |
| Calidad del `ANALISIS_MVVM.md` | 15 |
| Bonus — Parte 4 | +15 |

**Nota:** la cacería de bugs (30) vale más que la implementación (25). El taller evalúa si **entiendes el flujo unidireccional**, no solo si lograste que compilara.

---

## Lista de verificación antes de entregar

- [ ] `_elementos` es privado; `elementos` es público de tipo `StateFlow`.
- [ ] El estado de carga funciona con `delay(1000)`.
- [ ] La lista ya no está hardcodeada en el Composable.
- [ ] `marcarFavorito` es una función pública del ViewModel.
- [ ] No hay ninguna variable de estado duplicada fuera del `StateFlow`.
- [ ] Reproduje el bug de la Parte 2 y lo documenté.
- [ ] Corregí el bug y verifiqué que la UI se actualiza.
- [ ] Los favoritos sobreviven a la rotación.
- [ ] `ANALISIS_MVVM.md` tiene la predicción, el diagnóstico y la reflexión.
- [ ] El ViewModel no importa ninguna clase de `androidx.compose.*`.
- [ ] No hay `!!` en el código.

---

## Entrega

Sube un único archivo (`.zip` o enlace de repositorio) según la fecha indicada por el docente.
