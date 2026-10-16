# Taller 4 — Pantalla de perfil de usuario en Jetpack Compose
## Programación para Dispositivos Móviles — Universidad de Caldas
**Clase 05** · Unidad 2 · Trabajo individual
**Puntaje base:** 100 puntos · **Bonus:** +15 puntos
**Tiempo estimado:** 5–6 horas de trabajo independiente
**Entrega:** repositorio Git con el proyecto + `ANALISIS.md` + capturas de pantalla

> **Créditos y tiempo de trabajo independiente:** el curso tiene 3 créditos. Bajo la norma de la Universidad de Caldas (1 crédito ≈ 48 horas de trabajo académico/semestre, en proporción aproximada 1:2 entre horas de acompañamiento directo y horas de trabajo autónomo), cada semana implica ~3 horas de clase presencial y **~6 horas de trabajo independiente**. Este taller está diseñado para consumir esas 6 horas fuera de clase (o completarse parcialmente en el bloque práctico de la sesión).

---

## Cómo está organizado este taller

Este taller está diseñado como un **playground guiado**: cada parte tiene micro-pasos numerados, y cada paso tiene tres cosas:

- **Qué hacer** — la acción concreta.
- **El código** — lo que debes escribir.
- **Qué deberías ver** — el resultado esperado, para que sepas si vas bien.

Al final de cada parte hay un **Checkpoint**: si no lo cumples, no avances a la siguiente parte.

> **Regla del taller:** no copies y pegues bloques completos. Escribe el código, ejecútalo y **rompe cosas a propósito** para entender qué hace cada línea. La Parte 2 y la Parte 3 se califican por tu **razonamiento**, no por el código final.

### Orden de trabajo obligatorio

| Parte | Contenido | Puntos | Cuándo hacerla |
|---|---|---:|---|
| **0** | Predicción escrita (antes de programar) | — | **Antes de escribir una sola línea** |
| **1** | Layout estático de la pantalla de perfil | 15 | Primero |
| **2** | Cacería de bugs: la lista que no se actualiza | 25 | Después de la Parte 1 |
| **3** | Estado que sobrevive a la rotación | 30 | Después de la Parte 2 |
| **4** | Bonus: animación y pulido | +15 | Solo si 1–3 están completas |
| **5** | `ANALISIS.md` | 20 | A lo largo de todo el taller |
| **6** | Validación de nombre no vacío | 10 | Integrada en la Parte 3 |

---

## Parte 0 — Predicción escrita (obligatoria, antes de programar)

**No abras Android Studio todavía.** Abre un archivo nuevo llamado `ANALISIS.md` y responde:

```markdown
# Análisis — Taller 4

## Predicción inicial (escrita ANTES de programar)

1. Si uso `remember { mutableStateOf(0) }` para un contador y roto la pantalla,
   ¿el valor se mantiene o se pierde? ¿Por qué?

2. Si tengo una lista creada con `remember { mutableListOf(...) }` y le agrego
   un elemento con `.add()`, ¿la pantalla se actualiza? ¿Por qué?

3. ¿Qué diferencia hay entre `remember` y `rememberSaveable`?
```

> **Importante:** estas respuestas se califican como parte de los 20 puntos del `ANALISIS.md`. No las corrijas después: el valor está en comparar lo que creías con lo que comprobaste. Al final del taller agregarás una sección **"Verificación"** con lo que realmente ocurrió.

---

## Parte 1 — Layout estático de la pantalla de perfil (15 pts)

**Objetivo:** construir la estructura visual completa, **sin interactividad todavía**.

### Paso 1.1 — Crea el proyecto

1. Android Studio → **New Project** → plantilla **Empty Activity**.
2. Nombre: `PerfilUsuario`. Package: `com.ucaldas.perfil`. Lenguaje: **Kotlin**. Minimum SDK: **API 24**.
3. Espera a que termine el *Gradle sync* y presiona **Run** una vez para confirmar que la plantilla funciona.

**Qué deberías ver:** la app abre con el texto "Hello Android!" centrado.

### Paso 1.2 — Limpia la plantilla

Borra el Composable `Greeting` y su `@Preview` que trae la plantilla. Deja `MainActivity` así:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PerfilScreen()
                }
            }
        }
    }
}
```

> **Nota:** `Surface` es el contenedor que aplica el color de fondo del tema. Sin él, el fondo puede verse transparente o negro.

### Paso 1.3 — Crea el esqueleto de `PerfilScreen`

```kotlin
@Composable
fun PerfilScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Aquí van los elementos, paso a paso
    }
}
```

**Qué deberías ver:** una pantalla vacía. Agrega un `@Preview` para iterar sin instalar:

```kotlin
@Preview(showBackground = true, name = "Perfil")
@Composable
fun PerfilScreenPreview() {
    MaterialTheme { PerfilScreen() }
}
```

### Paso 1.4 — El avatar circular

Este es el paso más importante de la Parte 1. Necesitas **superponer** un texto sobre un círculo, y para eso se usa `Box`.

```kotlin
Box(
    modifier = Modifier
        .size(96.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer),
    contentAlignment = Alignment.Center
) {
    Text(
        text = "AG",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}
```

**Qué deberías ver:** un círculo de color con las iniciales "AG" centradas.

> **El orden de la cadena es obligatorio:** `.size()` → `.clip()` → `.background()`. Si pones `background` antes de `clip`, el fondo será cuadrado y el recorte no se verá. **Pruébalo** para comprobarlo.

### Paso 1.5 — Nombre y carrera

```kotlin
Spacer(modifier = Modifier.height(12.dp))

Text(
    text = "Ana Gómez",
    style = MaterialTheme.typography.headlineSmall
)

Text(
    text = "Ingeniería de Sistemas",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant
)
```

**Qué deberías ver:** el nombre en grande y la carrera en gris debajo.

### Paso 1.6 — La tarjeta de publicaciones (estática)

```kotlin
Spacer(modifier = Modifier.height(20.dp))

Card(
    modifier = Modifier.fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Publicaciones", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "0",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
```

**Qué deberías ver:** una tarjeta con sombra, título "Publicaciones" y un "0" grande en color primario.

### Paso 1.7 — La sección de logros (estática)

```kotlin
Spacer(modifier = Modifier.height(24.dp))

Text("Logros", style = MaterialTheme.typography.titleMedium)
Spacer(modifier = Modifier.height(8.dp))
Text("• Primer commit", style = MaterialTheme.typography.bodyMedium)
```

### Checkpoint de la Parte 1

- [ ] El avatar es un **círculo** (no un cuadrado) con las iniciales centradas.
- [ ] El nombre usa `headlineSmall` y la carrera `bodyMedium` con `onSurfaceVariant`.
- [ ] La tarjeta tiene elevación y su padding está **adentro** (el color llega al borde).
- [ ] Usaste al menos **3 `Modifier` distintos** y sabes explicar qué hace cada uno.
- [ ] El `@Preview` se ve correctamente.

> **Entrega de esta parte:** captura de pantalla del `@Preview` o del emulador.

---

## Parte 2 — Cacería de bugs: la lista que no se actualiza (25 pts)

**Objetivo:** diagnosticar y corregir un bug clásico de Compose. **El diagnóstico vale más que el arreglo.**

### Paso 2.1 — Reproduce el bug

Reemplaza la sección de logros estática por esta versión **con el bug**:

```kotlin
@Composable
fun SeccionLogros(logros: MutableList<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Logros", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        logros.forEach { logro ->
            Text(text = "• $logro", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { logros.add("Nuevo logro ${logros.size + 1}") }) {
            Text("Agregar logro")
        }
    }
}
```

Y en `PerfilScreen`, agrega:

```kotlin
val logros = remember { mutableListOf("Primer commit") }
// ...
SeccionLogros(logros = logros)
```

### Paso 2.2 — Observa el bug

1. Ejecuta la app.
2. Presiona **"Agregar logro"** varias veces.

**Qué deberías ver:** **nada cambia en pantalla.** La lista se ve igual, aunque el botón sí se presiona.

### Paso 2.3 — Diagnostica (esto es lo que se califica)

Antes de arreglar nada, escribe en `ANALISIS.md`:

```markdown
## Diagnóstico del bug (Parte 2)

### Síntoma
Al presionar "Agregar logro", la lista no se actualiza en pantalla.

### ¿Por qué ocurre?
[Explica con tus palabras por qué `mutableListOf` no dispara recomposición.
Menciona: qué tipo de objeto devuelve, si Compose lo observa o no,
y qué papel juega `remember` en todo esto.]

### Evidencia
[¿Cómo comprobaste que el elemento SÍ se agregó a la lista aunque la
pantalla no cambiara? Sugerencia: agrega un Log.d dentro del onClick
y observa Logcat.]
```

**Pista para la evidencia:** agrega temporalmente esto dentro del `onClick`:

```kotlin
OutlinedButton(onClick = {
    logros.add("Nuevo logro ${logros.size + 1}")
    Log.d("Taller4", "Tamaño de la lista: ${logros.size}")
}) { Text("Agregar logro") }
```

Verás en Logcat que el tamaño **sí aumenta** (1, 2, 3...), pero la pantalla no cambia. Eso demuestra que el problema **no es la lista**, es que Compose no se enteró.

### Paso 2.4 — Corrige

**Opción A (recomendada) — `mutableStateListOf`:**

```kotlin
val logros = remember { mutableStateListOf("Primer commit") }
```

**Opción B — lista inmutable reasignada:**

```kotlin
var logros by remember { mutableStateOf(listOf("Primer commit")) }
// y en el onClick:
logros = logros + "Nuevo logro ${logros.size + 1}"
```

**Qué deberías ver:** ahora al presionar el botón, el nuevo logro **aparece inmediatamente** en la lista.

### Paso 2.5 — Documenta la corrección

```markdown
### Corrección aplicada
[¿Cuál opción usaste y por qué?]

### ¿Por qué funciona?
[Explica la diferencia entre `mutableListOf` y `mutableStateListOf`.
Menciona el concepto de lista observable / SnapshotStateList.]
```

### Checkpoint de la Parte 2

- [ ] Reprodujiste el bug y lo viste con tus propios ojos.
- [ ] Comprobaste con Logcat que el elemento **sí** se agregaba a la lista.
- [ ] Explicaste **por qué** `mutableListOf` no notifica a Compose.
- [ ] La corrección funciona: los logros aparecen al instante.
- [ ] Documentaste síntoma, causa, evidencia y corrección en `ANALISIS.md`.

> **Entrega de esta parte:** captura **antes** (lista que no cambia) y **después** (lista que sí cambia), más la sección de diagnóstico.

---

## Parte 3 — Estado que sobrevive a la rotación (30 pts)

**Objetivo:** demostrar experimentalmente la diferencia entre `remember` y `rememberSaveable`.

### Paso 3.1 — Convierte el contador en interactivo

Reemplaza el `Text("0")` de la tarjeta por un contador con botones:

```kotlin
var publicaciones by remember { mutableStateOf(0) }

// dentro de la Card:
Text(
    text = "$publicaciones",
    style = MaterialTheme.typography.displaySmall,
    color = MaterialTheme.colorScheme.primary
)
Spacer(modifier = Modifier.height(12.dp))
Row(verticalAlignment = Alignment.CenterVertically) {
    Button(onClick = { if (publicaciones > 0) publicaciones-- }) { Text("-") }
    Spacer(modifier = Modifier.width(16.dp))
    Button(onClick = { publicaciones++ }) { Text("+") }
}
```

**Qué deberías ver:** los botones `+` y `-` cambian el número al instante.

### Paso 3.2 — El experimento de la rotación (con `remember`)

1. Con `remember`, presiona `+` hasta llegar a **5**.
2. Rota el emulador (`Ctrl`+`←`/`→` en la barra lateral, o el botón de rotación).
3. Observa el contador.

**Qué deberías ver:** el contador **vuelve a 0**. Ese es el bug.

**Captura esta pantalla.** Es la evidencia del "antes".

### Paso 3.3 — Corrige con `rememberSaveable`

```kotlin
var publicaciones by rememberSaveable { mutableStateOf(0) }
```

Repite el experimento: llega a 5, rota, observa.

**Qué deberías ver:** el contador **se mantiene en 5**.

**Captura esta pantalla.** Es la evidencia del "después".

### Paso 3.4 — Aplica lo mismo al resto del estado

Cambia también a `rememberSaveable`:

```kotlin
var nombre by rememberSaveable { mutableStateOf("Ana Gómez") }
var editando by rememberSaveable { mutableStateOf(false) }
var borrador by rememberSaveable { mutableStateOf("") }
```

> **Ojo:** `mutableStateListOf` **no** funciona con `rememberSaveable` directamente. Deja la lista con `remember` y anótalo en el análisis como limitación conocida.

### Paso 3.5 — Agrega el formulario de edición

```kotlin
Spacer(modifier = Modifier.height(16.dp))

Button(onClick = {
    borrador = nombre
    editando = !editando
}) {
    Text(if (editando) "Cancelar" else "Editar perfil")
}

if (editando) {
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = borrador,
        onValueChange = { borrador = it },
        label = { Text("Nombre") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        onClick = {
            nombre = borrador.ifBlank { "Usuario sin nombre" }
            editando = false
        },
        modifier = Modifier.fillMaxWidth()
    ) { Text("Guardar") }
}
```

**Qué deberías ver:** el botón "Editar perfil" muestra un campo de texto; al escribir y presionar "Guardar", el nombre de arriba cambia.

> **Si el campo no deja escribir:** revisa que `onValueChange = { borrador = it }` esté actualizando el estado. Sin eso, el `TextField` no acepta escritura.

### Paso 3.6 — Validación de nombre no vacío (10 pts)

El encabezado debe mostrar un texto por defecto si el nombre queda vacío:

```kotlin
Text(
    text = nombre.ifBlank { "Usuario sin nombre" },
    style = MaterialTheme.typography.headlineSmall
)
```

**Pruébalo:** edita el nombre, borra todo el texto, presiona "Guardar". El encabezado debe decir **"Usuario sin nombre"**, no quedar vacío.

### Paso 3.7 — Documenta el experimento

```markdown
## Experimento de rotación (Parte 3)

### Con `remember`
[¿Qué pasó al rotar? ¿Por qué?]

### Con `rememberSaveable`
[¿Qué pasó al rotar? ¿Por qué?]

### Explicación técnica
[Relaciona esto con el ciclo de vida de la Activity visto en la Clase 04:
qué pasa con `onDestroy`/`onCreate` al rotar, y cómo `rememberSaveable`
usa el Bundle de estado para restaurar el valor.]
```

### Checkpoint de la Parte 3

- [ ] Tienes captura del contador **perdiéndose** con `remember`.
- [ ] Tienes captura del contador **conservándose** con `rememberSaveable`.
- [ ] El formulario de edición funciona y el `TextField` acepta escritura.
- [ ] El nombre vacío muestra "Usuario sin nombre".
- [ ] Explicaste la relación con el ciclo de vida de la Activity.

> **Entrega de esta parte:** las dos capturas + la sección del experimento en `ANALISIS.md`.

---

## Parte 4 — Bonus: animación y pulido (+15 pts)

> **Solo se otorga si las Partes 1, 2 y 3 están completas.**

### Bonus A — Animación de aparición (+8)

Reemplaza el `if (editando) { ... }` por `AnimatedVisibility`:

```kotlin
import androidx.compose.animation.AnimatedVisibility

AnimatedVisibility(visible = editando) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        OutlinedTextField(
            value = borrador,
            onValueChange = { borrador = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                nombre = borrador.ifBlank { "Usuario sin nombre" }
                editando = false
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar") }
    }
}
```

**Qué deberías ver:** el formulario aparece y desaparece con una animación suave en vez de un salto brusco.

### Bonus B — Botón deshabilitado (+4)

```kotlin
Button(
    onClick = { if (publicaciones > 0) publicaciones-- },
    enabled = publicaciones > 0
) { Text("-") }
```

**Qué deberías ver:** cuando el contador está en 0, el botón `-` se ve gris y no responde.

### Bonus C — Avatar dinámico (+3)

Genera las iniciales a partir del nombre:

```kotlin
private fun inicialesDe(nombre: String): String =
    nombre.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "?" }
```

Y úsala: `Avatar(iniciales = inicialesDe(nombre))`.

**Qué deberías ver:** al cambiar el nombre a "Carlos Pérez", el avatar muestra "CP".

---

## Parte 5 — `ANALISIS.md` (20 pts)

Este archivo es **la mitad del valor del taller**. Debe contener:

```markdown
# Análisis — Taller 4

## 1. Predicción inicial (escrita antes de programar)
[Las 3 respuestas de la Parte 0, sin modificar]

## 2. Verificación de la predicción
[Compara lo que predijiste con lo que realmente ocurrió.
Sé honesto: si te equivocaste, dilo y explica por qué.]

## 3. Diagnóstico del bug (Parte 2)
[Síntoma, causa, evidencia con Logcat, corrección y por qué funciona]

## 4. Experimento de rotación (Parte 3)
[Comportamiento con `remember`, con `rememberSaveable`, y explicación
técnica relacionada con el ciclo de vida de la Activity]

## 5. Justificación de los Modifier usados
[Elige 3 Modifier de tu código y explica por qué están en ese orden
y qué pasaría si los movieras]

## 6. Dificultades encontradas
[Qué te costó más y cómo lo resolviste]
```

### Criterios de calidad

| Nivel | Descripción |
|---|---|
| **Excelente (18–20)** | Explica mecanismos internos (snapshot, Bundle, recomposición) con precisión y usa evidencia propia |
| **Bueno (14–17)** | Explica correctamente el comportamiento observable, con algo de evidencia |
| **Aceptable (10–13)** | Describe qué pasó pero sin explicar el por qué |
| **Insuficiente (0–9)** | Respuestas de una línea, copiadas de la guía, o sin evidencia |

---

## Parte 6 — Entrega

### Qué entregar

1. **Repositorio Git** con el proyecto completo (código compilando).
2. **`ANALISIS.md`** en la raíz del repositorio.
3. **Carpeta `capturas/`** con:
   - `01_layout_estatico.png` — Parte 1
   - `02_bug_antes.png` — lista que no se actualiza
   - `03_bug_despues.png` — lista que sí se actualiza
   - `04_rotacion_remember.png` — contador en 0 tras rotar
   - `05_rotacion_saveable.png` — contador conservado tras rotar
   - `06_nombre_vacio.png` — validación de nombre no vacío
   - (bonus) `07_animacion.gif` o captura del formulario animado

### Cómo nombrar el repositorio

```text
taller4-perfil-compose-apellido-nombre
```

### Checklist final antes de entregar

- [ ] El proyecto **compila** sin errores.
- [ ] La app corre en el emulador sin crashear.
- [ ] El avatar es circular y muestra iniciales.
- [ ] El contador usa `rememberSaveable` y sobrevive a la rotación.
- [ ] La lista usa `mutableStateListOf` (o lista inmutable reasignada) y se actualiza.
- [ ] El `TextField` acepta escritura y el nombre se actualiza al guardar.
- [ ] El nombre vacío muestra "Usuario sin nombre".
- [ ] `ANALISIS.md` está completo, con las 6 secciones.
- [ ] Las capturas están en la carpeta `capturas/`.
- [ ] El código tiene comentarios explicando los `Modifier` no obvios.

---

## Rúbrica

| Criterio | Puntos | Cómo se pierde el puntaje |
|---|---:|---|
| **Parte 1** — Layout estático completo y correcto | 15 | Falta el avatar circular; el layout no usa `Column`/`Row`/`Box` correctamente; el padding de la `Card` está mal ubicado |
| **Parte 2** — Diagnóstico y corrección del bug | 25 | Corrige el código pero **no explica** por qué `mutableListOf` no notifica. El diagnóstico vale más que el arreglo |
| **Parte 3** — `rememberSaveable` comprobado con evidencia | 30 | Usa `remember`; o usa `rememberSaveable` pero no adjunta las capturas de la comparación |
| **Parte 6** — Validación de nombre no vacío | 10 | El encabezado queda vacío al borrar el texto |
| **Parte 5** — `ANALISIS.md`: calidad de las justificaciones | 20 | Respuestas de una línea sin razonamiento técnico ni evidencia |
| **Parte 4** — Bonus | +15 | Solo se otorga si las Partes 1–3 están completas |

**Total base: 100 puntos. Máximo con bonus: 115 puntos.**

> **Sobre el criterio de 30 puntos:** exige **evidencia** (capturas de ambos casos). Escribir `rememberSaveable` sin demostrar la diferencia no cumple el objetivo, que es **entender** el mecanismo, no memorizar la API.

---

## Preguntas frecuentes

**¿Puedo usar `mutableStateOf(listOf(...))` en vez de `mutableStateListOf`?**
Sí, es la Opción B y es igualmente válida. Solo recuerda que debes **reasignar** la lista (`logros = logros + x`), no mutarla.

**¿Por qué `mutableStateListOf` no sobrevive a la rotación?**
Porque `rememberSaveable` necesita saber cómo guardar el valor en el `Bundle`, y una lista de strings no es un tipo soportado por defecto. Se resuelve con un `Saver` o con un `ViewModel` (Clase 08). Menciónalo en el análisis como limitación conocida.

**¿Necesito usar `Icons.Default.Person` para el avatar?**
No. El avatar con iniciales es suficiente y más simple. Si quieres usar un ícono, necesitas `import androidx.compose.material.icons.Icons` y `import androidx.compose.material.icons.filled.Person`.

**¿El `@Preview` cuenta como evidencia?**
Para la Parte 1 sí. Para las Partes 2 y 3 **no**: necesitas capturas del emulador, porque el `@Preview` no reproduce la rotación ni el estado en tiempo real.

**¿Puedo cambiar los colores o el diseño?**
Sí, siempre que uses `MaterialTheme.colorScheme` en vez de colores fijos. Un diseño propio bien justificado suma en la Parte 5.
