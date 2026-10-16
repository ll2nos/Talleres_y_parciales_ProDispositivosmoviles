# Guía del Estudiante — Clase 05: UI Moderna con Jetpack Compose I
## Programación para Dispositivos Móviles — Universidad de Caldas
**Unidad 2** · Semana 5 · Duración: 3 horas de clase + ~6 horas de trabajo independiente
**Herramientas:** Android Studio, emulador con API 34+, Logcat
**Entregable:** Taller 4 — Pantalla de perfil de usuario en Jetpack Compose

---

## Cómo usar esta guía

Esta guía está pensada para que la sigas **con Android Studio abierto al lado**. Tiene tres tipos de contenido:

- **Conceptos** — explicaciones con analogías, para entender el *por qué*.
- **Playground** — bloques de código que puedes copiar, pegar y ejecutar para experimentar.
- **Checkpoints** — preguntas que debes poder responder antes de seguir. Si no puedes, vuelve atrás.

> **Consejo:** no leas esta guía de corrido. Escribe el código, ejecútalo, rómpelo a propósito y observa qué pasa. Compose se aprende experimentando.

---

## 1. ¿Qué voy a aprender hoy?

Al terminar la clase serás capaz de:

1. Explicar por qué Compose es **declarativo** y en qué se diferencia de construir vistas a mano con XML.
2. Escribir tus propias funciones `@Composable` con `Text`, `Column`, `Row`, `Box`, `Spacer` y `Button`.
3. Usar `Modifier` para tamaño, espaciado, fondo, recorte y clicks, entendiendo que **el orden importa**.
4. Manejar **estado** con `remember` y `mutableStateOf`, y entender qué es una **recomposición**.
5. Distinguir `remember` de `rememberSaveable` y saber cuál usar en cada caso.
6. Reconocer por qué una lista mutable normal **no** actualiza la pantalla, y cómo arreglarlo.

---

## 2. Antes de la clase (15 min de preparación)

### 2.1 Verifica tu entorno

- [ ] Android Studio actualizado, con un proyecto **Empty Activity** creado y compilado al menos una vez.
- [ ] Emulador encendido y con la app instalada.
- [ ] Sabes abrir **Logcat** (barra inferior de Android Studio, pestaña "Logcat").
- [ ] Sabes rotar el emulador (botón de rotación en la barra lateral, o `Ctrl`+`←`/`→`).

### 2.2 Repasa de la Clase 04

Compose **vive dentro de una Activity**, no la reemplaza. Recuerda:

- `onCreate` es donde se configura la pantalla.
- Rotar el teléfono es un **cambio de configuración**: Android destruye la Activity (`onDestroy`) y crea una nueva (`onCreate`).
- `onSaveInstanceState` es el mecanismo clásico para guardar datos antes de esa destrucción.

> **Guarda esta idea.** Es la clave para entender `rememberSaveable` más adelante en la clase.

### 2.3 Predicción inicial (hazlo antes de programar)

Escribe en tu cuaderno, sin buscar en internet:

1. Si uso `remember { mutableStateOf(0) }` para un contador y **roto la pantalla**, ¿el valor se mantiene o se pierde? ¿Por qué?
2. ¿Qué crees que hace exactamente la palabra `remember`?

Al final de la clase volverás a estas respuestas. **No las borres**: el contraste entre lo que creías y lo que es, es el aprendizaje más valioso de hoy.

---

## 3. Concepto 1 — Declarativo vs imperativo

### 3.1 El problema de la forma clásica

En la Clase 04 viste código así:

```kotlin
val tvNombre = findViewById<TextView>(R.id.tvNombre)
val btnSumar = findViewById<Button>(R.id.btnSumar)
var contador = 0

btnSumar.setOnClickListener {
    contador++
    tvNombre.text = "Publicaciones: $contador"   // hay que ACORDARSE de actualizar
}
```

**El problema:** tú eres responsable de mantener sincronizados **el dato** y **la pantalla**. Si olvidas la línea `tvNombre.text = ...`, el dato cambia pero la pantalla no. Esto se llama programación **imperativa**: le das órdenes paso a paso a la interfaz.

### 3.2 La idea de Compose

```kotlin
var contador by remember { mutableStateOf(0) }

Text(text = "Publicaciones: $contador")
Button(onClick = { contador++ }) { Text("+") }
```

Aquí **no le dices a la pantalla qué hacer**. Describes **cómo se ve la pantalla para un valor de `contador`**. Cuando `contador` cambia, Compose vuelve a ejecutar la descripción y actualiza lo necesario.

Esto es programación **declarativa**: describes el resultado, no los pasos.

### 3.3 Comparación directa

| Pregunta | Views/XML (imperativo) | Compose (declarativo) |
|---|---|---|
| ¿Dónde se define la UI? | En XML (`activity_main.xml`) | En funciones Kotlin |
| ¿Cómo se conecta con el código? | `findViewById(R.id...)` | No se conecta: es el mismo código |
| ¿Cómo se actualiza? | Tú llamas `setText`, `setVisibility`... | Cambias el estado y Compose recompone |
| ¿Cómo se reutiliza? | `<include>`, layouts duplicados | Llamas la función con otros parámetros |
| ¿Cómo se previsualiza? | Preview del layout XML | `@Preview` sobre el Composable |

### 3.4 Por qué esto importa

Como la UI es **código Kotlin normal**, puedes usar `if`, `for`, `when` y funciones dentro de ella. Mostrar u ocultar algo ya no es `setVisibility(View.GONE)`, es simplemente:

```kotlin
if (editando) {
    FormularioEdicion()
}
```

**Checkpoint 1:** explica en una frase la diferencia entre "actualizar la vista a mano" y "describir la UI según el estado".

---

## 4. Playground 1 — Tu primer Composable

### Paso 1 — Abre `MainActivity.kt`

La plantilla "Empty Activity" ya trae esto:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SaludoScreen(nombre = "Universidad de Caldas")
            }
        }
    }
}
```

**`setContent { }` es el reemplazo directo de `setContentView(R.layout.activity_main)`.** Ya no hay XML que inflar.

### Paso 2 — Escribe tu primer Composable

```kotlin
@Composable
fun SaludoScreen(nombre: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hola, $nombre",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
```

### Paso 3 — Entiende la anotación `@Composable`

`@Composable` es una etiqueta que le dice al compilador:

> "Esta función no devuelve nada, pero **describe UI**."

Por eso el compilador la trata distinto: le permite llamar a otras funciones `@Composable` y le inyecta el motor de recomposición.

**Tres reglas de oro:**

1. Un Composable **no devuelve un valor** (devuelve `Unit`).
2. Solo puedes llamar a un Composable desde **otro Composable**.
3. Por convención, los Composables se nombran en **PascalCase** (`SaludoScreen`), como si fueran tipos.

### Paso 4 — Ejecuta

Presiona Run. Deberías ver "Hola, Universidad de Caldas" centrado en la pantalla.

> **Si el IDE marca `Column` o `Text` en rojo:** es un import faltante. Pon el cursor sobre el símbolo y presiona `Alt`+`Enter` (Windows/Linux) u `Option`+`Enter` (Mac). Este es el error #1 al empezar con Compose.

### Paso 5 — Agrega un `@Preview` (¡no lo saltes!)

```kotlin
@Preview(showBackground = true, name = "Saludo")
@Composable
fun SaludoScreenPreview() {
    MaterialTheme {
        SaludoScreen(nombre = "Universidad de Caldas")
    }
}
```

**Qué hace:** dibuja el Composable en el panel de previsualización del IDE, **sin recompilar ni instalar** en el emulador. Es la herramienta que más tiempo te va a ahorrar en el taller.

**Prueba esto:** cambia `padding(24.dp)` por `padding(48.dp)` y observa cómo el preview se actualiza al instante.

**Checkpoint 2:** tienes la app corriendo **y** un `@Preview` funcionando.

---

## 5. Concepto 2 — Layout: Column, Row, Box

Toda pantalla en Compose se construye anidando **tres contenedores**:

```text
Column  → apila hijos VERTICALMENTE   (uno debajo de otro)
Row     → alinea hijos HORIZONTALMENTE (uno al lado de otro)
Box     → SUPERPONE hijos (uno encima de otro, como capas)
```

### Playground 2 — Los tres contenedores

```kotlin
@Composable
fun DemoContenedores() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Estoy en una Column (arriba)")

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Izquierda")
            Spacer(modifier = Modifier.width(16.dp))
            Text("Derecha")
        }

        Box(modifier = Modifier.size(120.dp)) {
            Text("Capa de abajo")
            Text("Capa de arriba", modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}
```

**`Spacer`** es un Composable vacío cuyo único propósito es ocupar espacio:

```kotlin
Spacer(modifier = Modifier.height(16.dp))   // espacio vertical
Spacer(modifier = Modifier.width(16.dp))    // espacio horizontal
```

**¿Por qué te importa `Box`?** Porque es la única forma de poner un texto **encima** de un círculo de color. Lo usarás para el avatar del taller.

### Alineación

```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally,  // centra horizontalmente
    verticalArrangement = Arrangement.Center             // centra verticalmente
) { /* ... */ }
```

- En `Column`, `horizontalAlignment` controla el eje **horizontal** y `verticalArrangement` el **vertical**.
- En `Row` es al revés: `verticalAlignment` (vertical) y `horizontalArrangement` (horizontal).

**Checkpoint 3:** ¿por qué usamos `Box` y no `Column` para el avatar circular? → *Porque necesitamos superponer el texto sobre el círculo.*

---

## 6. Concepto 3 — Modifier y el orden

`Modifier` es una **cadena de configuraciones** que se aplica a un Composable. Se lee de arriba hacia abajo, como capas de cebolla: **el primero envuelve a los siguientes**.

### Playground 3 — El orden cambia el resultado

**Versión A — `padding` antes de `background`:**

```kotlin
Text(
    text = "Programación para Dispositivos Móviles",
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)                  // primero el padding
        .background(Color(0xFFF3F2EE)),  // luego el fondo
    textAlign = TextAlign.Center
)
```
→ El fondo **no** cubre los 16 dp de padding: queda un margen sin color alrededor.

**Versión B — `background` antes de `padding`:**

```kotlin
Text(
    text = "Programación para Dispositivos Móviles",
    modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF3F2EE))   // primero el fondo
        .padding(16.dp),                 // luego el padding
    textAlign = TextAlign.Center
)
```
→ El fondo **sí** cubre todo el ancho, y el texto queda con aire adentro.

**Pruébalo tú mismo.** Este es el mejor ejemplo de que en Compose el orden no es cosmético.

### Tabla de modificadores esenciales

| Modifier | Qué hace | Ejemplo |
|---|---|---|
| `fillMaxSize()` | Ocupa todo el ancho y alto disponibles | `Modifier.fillMaxSize()` |
| `fillMaxWidth()` | Ocupa todo el ancho disponible | `Modifier.fillMaxWidth()` |
| `size(w, h)` / `size(d)` | Tamaño fijo | `Modifier.size(120.dp)` |
| `padding(n)` | Espacio **alrededor** del contenido | `Modifier.padding(16.dp)` |
| `background(color)` | Pinta el fondo | `Modifier.background(Color.LightGray)` |
| `clip(shape)` | Recorta a una forma | `Modifier.clip(CircleShape)` |
| `border(w, color, shape)` | Dibuja un borde | `Modifier.border(2.dp, Color.Gray, CircleShape)` |
| `clickable { }` | Hace clickeable cualquier Composable | `Modifier.clickable { ... }` |
| `weight(f)` | Reparte el espacio **dentro de** `Row`/`Column` | `Modifier.weight(1f)` |
| `align(a)` | Alinea **dentro de** un `Box` | `Modifier.align(Alignment.Center)` |
| `alpha(f)` | Transparencia (0f–1f) | `Modifier.alpha(0.5f)` |

> **Ojo con `weight`:** solo funciona dentro de `Row` o `Column`. Si lo usas dentro de un `Box`, no compila.

**Checkpoint 4:** ¿por qué `padding` antes de `background` deja el fondo por dentro del margen?

---

## 7. Playground 4 — Tarjeta de perfil estática

Construye esto **sin interactividad todavía**. Usa `@Preview` para iterar.

```kotlin
@Composable
fun TarjetaPerfilEstatica() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar circular con inicial
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ana Gómez", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Ingeniería de Sistemas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

**Observa la cadena del avatar:** `.size(96.dp)` → `.clip(CircleShape)` → `.background(...)`. El orden es obligatorio: si pones `background` antes de `clip`, el fondo será cuadrado y el recorte no se verá.

---

## 8. Concepto 4 — Estado y recomposición

### 8.1 El problema: las variables normales no avisan

Escribe esto y ejecútalo. **El botón no hará nada visible:**

```kotlin
@Composable
fun ContadorRoto() {
    var contador = 0          // variable normal: Compose NO la observa

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Contador: $contador")
        Button(onClick = { contador++ }) { Text("+") }
    }
}
```

**¿Por qué no funciona?** Dos razones:

1. `contador` es una variable local de Kotlin. Compose **no tiene forma de saber** que cambió.
2. Aunque lo supiera, la función ya terminó de ejecutarse: nadie la vuelve a llamar.

### 8.2 La solución: `remember` + `mutableStateOf`

```kotlin
@Composable
fun ContadorScreen() {
    var contador by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Contador: $contador", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { contador-- }) { Text("-") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { contador++ }) { Text("+") }
        }
    }
}
```

### 8.3 Descompón la línea clave

```text
var contador  by  remember { mutableStateOf(0) }
     │         │      │            │
     │         │      │            └── el valor inicial y el contenedor observable
     │         │      └── "recuerda este valor entre recomposiciones"
     │         └── delegado: permite usar contador++ en vez de contador.value++
     └── variable local que lee el valor actual
```

- **`mutableStateOf(0)`** → crea un **contenedor observable**. Compose se suscribe a él. Cuando su valor cambia, Compose marca como "sucio" a todo Composable que lo haya leído.
- **`remember { }`** → guarda ese contenedor en la memoria del Composable. **Sin `remember`, cada recomposición crearía un contenedor nuevo y volvería a 0.**
- **`by`** → azúcar sintáctico. Sin `by` tendrías que escribir `contador.value++` y `contador.value` en todas partes.

**La misma cosa sin `by`:**

```kotlin
val contador = remember { mutableStateOf(0) }
Text(text = "Contador: ${contador.value}")
Button(onClick = { contador.value++ }) { Text("+") }
```

> **Error común:** mezclar las dos formas. Si usas `by`, necesitas estos imports (el IDE los agrega, pero si los borras el código no compila):
> ```kotlin
> import androidx.compose.runtime.getValue
> import androidx.compose.runtime.setValue
> ```

### 8.4 ¿Qué es una recomposición?

```text
1. Compose ejecuta ContadorScreen()  → lee contador (0) → dibuja "Contador: 0"
2. El usuario presiona "+"
3. onClick ejecuta contador++  → mutableStateOf notifica el cambio
4. Compose sabe QUÉ Composables leyeron ese estado → los marca como sucios
5. Compose vuelve a ejecutar SOLO esos Composables → dibuja "Contador: 1"
```

**Recomponer NO es redibujar toda la pantalla.** Compose lleva un registro de qué función leyó qué estado. Solo vuelve a ejecutar las funciones afectadas. Por eso es eficiente **por diseño**, no por optimización manual.

### Playground 5 — Ve la recomposición con tus propios ojos

Agrega un log dentro del Composable:

```kotlin
@Composable
fun ContadorScreen() {
    var contador by remember { mutableStateOf(0) }
    Log.d("Recomposicion", "ContadorScreen se ejecutó con contador=$contador")
    // ...
}
```

Abre Logcat, filtra por `Recomposicion` y presiona "+" varias veces. Verás **una línea nueva por cada click**.

Ahora agrega un Composable que **no** lee el contador:

```kotlin
@Composable
fun EncabezadoFijo() {
    Log.d("Recomposicion", "EncabezadoFijo se ejecutó (solo una vez)")
    Text("Mi perfil", style = MaterialTheme.typography.titleLarge)
}
```

Llama a ambos desde la pantalla y observa: el log del encabezado **no se repite**. Esa es la prueba de que Compose solo recompone lo necesario.

### 8.5 Preguntas de autoevaluación

1. ¿Qué pasa si quito `remember` y dejo solo `mutableStateOf(0)`?
2. ¿Qué pasa si quito `mutableStateOf` y dejo `remember { 0 }`?
3. ¿Por qué `EncabezadoFijo` no se recompone cuando cambia el contador?

<details>
<summary>Ver respuestas</summary>

1. El contador se reinicia a 0 en cada recomposición: se crea un contenedor nuevo cada vez.
2. El valor se recuerda, pero nadie notifica a Compose: la pantalla nunca se actualiza.
3. Porque no lee ese estado. Compose solo recompone quien leyó el estado que cambió.

</details>

**Checkpoint 5:** ves en Logcat que el log del contador se repite y el del encabezado no.

---

## 9. Concepto 5 — Material Design 3

### 9.1 `MaterialTheme` — el sistema de diseño

`MaterialTheme` es un **proveedor de valores de diseño**. Todo lo que está dentro de él puede leer:

```kotlin
MaterialTheme.colorScheme.primary          // color principal de la marca
MaterialTheme.colorScheme.onPrimary        // color de texto SOBRE primary
MaterialTheme.colorScheme.surfaceVariant   // fondo suave
MaterialTheme.colorScheme.onSurfaceVariant // texto sobre ese fondo
MaterialTheme.typography.headlineSmall     // escala tipográfica
MaterialTheme.typography.bodyMedium
MaterialTheme.typography.titleLarge
```

**La convención `onX` significa "color de contenido que va ENCIMA de X".** Usar `onPrimary` sobre `primary` garantiza contraste correcto en modo claro y oscuro automáticamente.

### 9.2 Escala tipográfica

No uses `fontSize` a mano. Usa la escala:

| Estilo | Uso típico |
|---|---|
| `displaySmall` | Números grandes (contadores) |
| `headlineSmall` / `headlineMedium` | Título de pantalla, nombre de usuario |
| `titleLarge` / `titleMedium` | Títulos de sección, encabezados de tarjeta |
| `bodyLarge` / `bodyMedium` | Texto de contenido |
| `labelSmall` | Etiquetas y textos auxiliares |

### 9.3 `Card`

```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = nombre, style = MaterialTheme.typography.titleLarge)
        Text(text = carrera, style = MaterialTheme.typography.bodyMedium)
    }
}
```

**El padding va adentro** (en el `Column` interno), no en la `Card`, para que el color de fondo llegue hasta el borde.

### 9.4 Botones

```kotlin
Button(onClick = { /* ... */ }) { Text("Guardar") }          // relleno, acción principal
OutlinedButton(onClick = { /* ... */ }) { Text("Cancelar") } // contorno, acción secundaria
TextButton(onClick = { /* ... */ }) { Text("Ver más") }      // sin fondo, acción terciaria
```

**Botón deshabilitado:**

```kotlin
Button(
    onClick = { contador-- },
    enabled = contador > 0        // se ve gris y no responde al click
) { Text("-") }
```

### 9.5 `TextField` — el componente controlado

```kotlin
var texto by remember { mutableStateOf("") }

OutlinedTextField(
    value = texto,
    onValueChange = { texto = it },     // patrón obligatorio: value + onValueChange
    label = { Text("Nombre") },
    singleLine = true,
    modifier = Modifier.fillMaxWidth()
)
```

**`TextField` es un componente controlado.** Tú eres dueño del texto (`value`), y el componente te avisa de cada cambio (`onValueChange`).

> **Error muy común:** si no actualizas `texto` dentro de `onValueChange`, el campo se ve pero **no acepta escritura**. El campo no guarda nada por sí solo: tú eres el dueño del estado.

**Checkpoint 6:** tienes un `OutlinedTextField` que permite escribir y refleja el texto en un `Text` de al lado.

---

## 10. Concepto 6 — `remember` vs `rememberSaveable`

### 10.1 El experimento

1. Con `var contador by remember { mutableStateOf(0) }`, incrementa hasta 5.
2. Rota el emulador.
3. **El contador vuelve a 0.**

### 10.2 ¿Por qué?

Rotar la pantalla es un **cambio de configuración**. Android destruye la Activity y crea una nueva (lo viste en la Clase 04: `onDestroy` → `onCreate`). Al destruirse la Activity, se destruye todo lo que estaba en memoria, incluido lo que `remember` guardaba.

> **`remember` sobrevive a recomposiciones, NO a la destrucción de la Activity.**

### 10.3 La solución

```kotlin
var contador by rememberSaveable { mutableStateOf(0) }
```

`rememberSaveable` guarda el valor en el `Bundle` de estado de la Activity (el mismo mecanismo de `onSaveInstanceState` de la Clase 04). Cuando la Activity se recrea, el valor se restaura.

### 10.4 Tabla de decisión

| | `remember` | `rememberSaveable` |
|---|---|---|
| Sobrevive a recomposición | Sí | Sí |
| Sobrevive a rotación / cambio de configuración | **No** | **Sí** |
| Sobrevive a muerte del proceso en segundo plano | No | Sí (vía `Bundle`) |
| Tipos soportados | Cualquiera | Primitivos, `String`, y tipos con `Saver` |
| Cuándo usarlo | Estado efímero (animaciones, scroll, expansión temporal) | Datos que el usuario espera conservar (texto escrito, contador, selección) |

**Regla práctica:** *si al usuario le molestaría perderlo, usa `rememberSaveable`.*

**Checkpoint 7:** puedes explicar por qué `remember` pierde el valor al rotar y `rememberSaveable` no.

---

## 11. Concepto 7 — El bug clásico de las listas

### 11.1 El código que "parece correcto"

```kotlin
@Composable
fun SeccionLogrosConBug() {
    val logros = remember { mutableListOf("Primer commit") }   // BUG

    Column {
        logros.forEach { Text(it) }
        Button(onClick = { logros.add("Nuevo logro ${logros.size + 1}") }) {
            Text("Agregar logro")
        }
    }
}
```

Presiona el botón: **la lista no se actualiza en pantalla**, aunque el código parece correcto.

### 11.2 ¿Por qué falla?

`mutableListOf` devuelve un `ArrayList` normal de Kotlin. **Compose no lo observa.** Cuando llamas `.add()`:

- La lista cambia internamente, pero **nadie le avisa a Compose**.
- La referencia al objeto es la misma: para Compose, "nada cambió".
- `remember` solo evita que la lista se recree; **no la vuelve observable**.

### 11.3 Dos correcciones válidas

**Opción 1 — `mutableStateListOf` (recomendada):**

```kotlin
val logros = remember { mutableStateListOf("Primer commit") }
// .add(), .remove(), .clear() ahora SÍ disparan recomposición
```

**Opción 2 — lista inmutable reemplazada completa:**

```kotlin
var logros by remember { mutableStateOf(listOf("Primer commit")) }
// logros = logros + "Nuevo logro"   ← se reemplaza el valor → sí notifica
```

### 11.4 Tabla de decisión

| | `mutableListOf` | `mutableStateListOf` | `mutableStateOf(listOf(...))` |
|---|---|---|---|
| ¿Dispara recomposición al mutar? | No | **Sí** | N/A (no se muta) |
| ¿Cómo se modifica? | `.add()`, `.remove()` | `.add()`, `.remove()` | Reasignando: `lista = lista + x` |
| Ideal para | Datos que no se muestran | Listas que se muestran y cambian | Listas pequeñas, cambios poco frecuentes |

> **Nota avanzada:** `mutableStateListOf` **no** es compatible con `rememberSaveable` directamente. Si necesitas que la lista sobreviva a la rotación, tendrás que usar un `Saver` o un `ViewModel` (lo verás en la Clase 08).

**Checkpoint 8:** puedes explicar por qué `mutableListOf` no actualiza la pantalla.

---

## 12. Concepto 8 — Estado elevado (*state hoisting*)

Un componente reutilizable **no debería tener su propio estado**. Debe recibir el valor y devolver eventos.

```kotlin
@Composable
fun ContadorPublicaciones(
    valor: Int,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onDecrementar, enabled = valor > 0) { Text("-") }
        Text(
            text = "$valor",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Button(onClick = onIncrementar) { Text("+") }
    }
}
```

El estado **sube** al padre:

```kotlin
@Composable
fun PantallaConEstado() {
    var publicaciones by rememberSaveable { mutableStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Publicaciones: $publicaciones")   // mismo estado, dos lugares
        ContadorPublicaciones(
            valor = publicaciones,
            onIncrementar = { publicaciones++ },
            onDecrementar = { if (publicaciones > 0) publicaciones-- }
        )
    }
}
```

**La frase que debes recordar:** *"El estado baja, los eventos suben."*

**Convención de nombres:**
- `valor` / `onValueChange`
- `visible` / `onVisibleChange`
- `nombre` / `onNombreChange`

Esto es la base de MVVM, que verás en la Clase 08.

---

## 13. Resumen de la clase

**Composable = función + estado + Modifier.**
La función describe, el estado dispara, el Modifier configura.

| Concepto | En una frase |
|---|---|
| `@Composable` | Función que describe una porción de UI; se puede volver a ejecutar (recomponer). |
| `remember` | Guarda un valor en memoria mientras el Composable permanece en pantalla. |
| `rememberSaveable` | Igual que `remember`, pero sobrevive a la rotación y a la muerte del proceso. |
| `mutableStateOf` | Contenedor de estado que notifica a Compose cuando su valor cambia. |
| `mutableStateListOf` | Lista observable: sus cambios sí disparan recomposición. |
| Recomposición | Volver a ejecutar los Composables cuyo estado leído cambió. |
| `Modifier` | Cadena ordenada de configuraciones (tamaño, padding, clicks) aplicada a un Composable. |
| `@Preview` | Dibuja un Composable en el IDE sin ejecutar la app. |
| *State hoisting* | El estado vive en el padre; el hijo recibe valor y emite eventos. |

---

## 14. Solución de problemas (troubleshooting)

| Síntoma | Causa probable | Solución |
|---|---|---|
| `Unresolved reference: Column` / `Text` | Falta el import | `Alt`+`Enter` / `Option`+`Enter` sobre el símbolo |
| El botón no hace nada visible | Variable normal en vez de `mutableStateOf` | `var x by remember { mutableStateOf(0) }` |
| El contador vuelve a 0 al rotar | Se usó `remember` | Cambiar a `rememberSaveable` |
| El contador se reinicia en cada click | Falta `remember` | `remember { mutableStateOf(0) }` |
| Error con `by` / `must be initialized` | Faltan los imports del delegado | `import androidx.compose.runtime.getValue` y `setValue` |
| `Cannot access 'value': it is private` | Se mezcló `by` con `.value` | Usar una sola forma, no ambas |
| El `TextField` no deja escribir | No se actualiza `value` en `onValueChange` | `onValueChange = { texto = it }` |
| La lista no se actualiza al agregar | `mutableListOf` dentro de `remember` | `mutableStateListOf` o reasignar la lista |
| `Modifier.weight` no compila | Se usó fuera de `Row`/`Column` | Moverlo dentro del scope correcto |
| El fondo no cubre el padding | Orden de `Modifier` | Poner `background` antes de `padding` |
| `@Preview` no se muestra | Falta la dependencia de tooling o no se compiló | Verificar `debugImplementation("androidx.compose.ui:ui-tooling")` y hacer *Rebuild* |
| Todo se ve en la esquina superior izquierda | Falta `fillMaxSize()` o la alineación | Agregar `Modifier.fillMaxSize()` y `horizontalAlignment` |
| `@Composable invocations can only happen from...` | Se llamó un Composable desde una función normal | Marcar la función contenedora con `@Composable` |

---

## 15. Glosario

| Término | Definición |
|---|---|
| **Composable** | Función anotada con `@Composable` que describe una porción de UI. |
| **Composición** | El árbol de Composables que Compose construye y mantiene. |
| **Recomposición** | Volver a ejecutar los Composables cuyo estado leído cambió. |
| **Estado** | Dato que, al cambiar, provoca que la UI se vuelva a dibujar. |
| **`remember`** | Guarda un valor en la memoria del Composable entre recomposiciones. |
| **`rememberSaveable`** | Como `remember`, pero el valor sobrevive a la rotación y a la muerte del proceso. |
| **`mutableStateOf`** | Crea un contenedor observable que notifica a Compose cuando cambia. |
| **`mutableStateListOf`** | Lista observable cuyas mutaciones disparan recomposición. |
| **`Modifier`** | Cadena ordenada de configuraciones aplicada a un Composable. |
| **`@Preview`** | Anotación que dibuja un Composable en el IDE sin ejecutar la app. |
| **`Saver`** | Mecanismo para indicar a `rememberSaveable` cómo guardar y restaurar un tipo propio. |
| **Cambio de configuración** | Evento (como rotar) que destruye y recrea la Activity. |
| **State hoisting** | Patrón en el que el estado vive en el padre y el hijo recibe valor + emite eventos. |
| **Material 3** | Sistema de diseño de Google: colores, tipografía y componentes. |
| **`colorScheme`** | Conjunto de colores del tema; la convención `onX` indica el color del contenido sobre `X`. |

---

## 16. Después de la clase

### 16.1 Revisa tu predicción inicial

Vuelve a las respuestas que escribiste en la sección 2.3. Compáralas con lo que aprendiste:

- ¿Acertaste sobre qué pasa al rotar con `remember`?
- ¿Tu definición de `remember` era correcta?

Si te equivocaste, **está bien**: ese es exactamente el punto de la clase. Escribe la corrección al lado.

### 16.2 Checklist de dominio

Antes de empezar el taller, verifica que puedes hacer todo esto **sin mirar la guía**:

- [ ] Escribir un Composable con `Column`, `Text` y `Spacer` desde cero.
- [ ] Explicar por qué el orden de `Modifier` importa, con un ejemplo.
- [ ] Escribir un contador con `remember` + `mutableStateOf` + `by`.
- [ ] Explicar qué es una recomposición y qué la dispara.
- [ ] Decidir entre `remember` y `rememberSaveable` para un caso dado.
- [ ] Explicar por qué `mutableListOf` no actualiza la pantalla.
- [ ] Escribir un `OutlinedTextField` controlado correctamente.

### 16.3 Ejercicios de refuerzo (opcionales)

1. **Modo oscuro:** cambia el tema del emulador a oscuro. ¿Tu pantalla se adapta? Si usaste `MaterialTheme.colorScheme`, sí. Si usaste `Color(0xFF...)` fijo, no. Corrige lo que haga falta.
2. **Rompe a propósito:** quita `remember` del contador y observa. Luego quita `mutableStateOf`. Anota qué pasa en cada caso.
3. **`weight`:** crea un `Row` con tres `Text` y dale `Modifier.weight(1f)` a cada uno. ¿Qué pasa si cambias uno a `weight(2f)`?
4. **`clickable`:** haz que un `Text` (no un `Button`) incremente el contador usando `Modifier.clickable { }`.

### 16.4 Siguiente clase

**Clase 06:** `LazyColumn` para listas eficientes y navegación entre pantallas con Navigation Compose.

Trae resuelto el **Taller 4** y tu archivo `ANALISIS.md` con la predicción inicial y el diagnóstico del bug.