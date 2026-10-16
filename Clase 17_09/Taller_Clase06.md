# Taller 5 — Reto: Lista con Navegación, Búsqueda y una Pila que No Debe Explotar
## Programación para Dispositivos Móviles — Universidad de Caldas
**Facultad de Ingeniería** · Unidad 2: Interfaces de Usuario
**Modalidad:** individual · **Nivel:** exigente

> **Créditos y tiempo de trabajo independiente:** el curso tiene 3 créditos. Bajo la norma de la Universidad de Caldas (1 crédito ≈ 48 horas de trabajo académico/semestre, en proporción aproximada 1:2 entre horas de acompañamiento directo y horas de trabajo autónomo), cada semana implica ~3 horas de clase presencial y **~6 horas de trabajo independiente**. Este taller está diseñado para consumir esas 6 horas fuera de clase (o completarse parcialmente en el bloque práctico de la sesión).

---

## Por qué este taller es más difícil de lo que parece

Cualquiera conecta una lista con un detalle. El reto real es que la navegación se comporte bien bajo uso repetido: sin duplicar pantallas en la pila, sin perder la posición de scroll, y con una búsqueda que realmente filtre sin romper la navegación.

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

Escribe tu predicción **antes** de codificar:

> Si el usuario toca la MISMA tarjeta tres veces rápido antes de que cargue la pantalla de detalle, ¿cuántas veces se navegará y cuántas pantallas de detalle quedarán apiladas?

La comprobarás en la Parte 3.

---

## Requisitos funcionales

1. Una lista de al menos 12 elementos de un tema libre, cada uno con al menos: `id`, `titulo`, `categoria`, `descripcionCorta` y `descripcionLarga`.
2. Pantalla de lista (`LazyColumn` + `items`) mostrando `titulo` y `descripcionCorta` en tarjetas (`Card`), con un campo de búsqueda que filtre por `titulo` en tiempo real.
3. Al tocar una tarjeta, navegar a la pantalla de detalle pasando el `id`.
4. Pantalla de detalle mostrando toda la información, con botón "Volver".

---

## Parte 1 — Datos y lista con filtro (2 horas aprox.)

### Paso 1.1 — Define el modelo
**Qué hacer:** crea la `data class` de tu modelo.

```kotlin
data class Elemento(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcionCorta: String,
    val descripcionLarga: String
)
```

**Checkpoint 1.1:** la `data class` compila y tiene los cinco campos.

### Paso 1.2 — Crea la lista de datos
**Qué hacer:** crea una lista de **12 o más** elementos, de **al menos 3 categorías distintas**.

```kotlin
val elementos = listOf(
    Elemento(1, "Elemento uno", "Categoría A", "Resumen corto", "Descripción larga..."),
    // ... hasta 12 o más
)
```

**Checkpoint 1.2:** `elementos.size >= 12` y hay al menos 3 valores distintos en `categoria`.

### Paso 1.3 — Construye la lista con `LazyColumn`
**Qué hacer:** crea `ListaScreen` con `LazyColumn` mostrando cada elemento en una `Card`.

```kotlin
@Composable
fun ListaScreen(elementos: List<Elemento>, onElementoClick: (Int) -> Unit) {
    LazyColumn {
        items(elementos, key = { it.id }) { elemento ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onElementoClick(elemento.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(elemento.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(elemento.descripcionCorta, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
```

**Qué deberías ver:** 12 tarjetas desplazables con título y descripción corta.

**Checkpoint 1.3:** la lista se desplaza suavemente y muestra los 12 elementos.

### Paso 1.4 — Agrega el campo de búsqueda
**Qué hacer:** agrega un `TextField` arriba de la lista. Mientras el usuario escribe, la lista debe filtrarse por `titulo`.

```kotlin
@Composable
fun ListaScreen(elementos: List<Elemento>, onElementoClick: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }

    val filtrados = remember(query) {
        if (query.isBlank()) elementos
        else elementos.filter { it.titulo.contains(query, ignoreCase = true) }
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        LazyColumn {
            items(filtrados, key = { it.id }) { elemento -> /* ... */ }
        }
    }
}
```

**Por qué `remember(query)`:** recalcula el filtro solo cuando `query` cambia, no en cada recomposición.

**Qué deberías ver:** al escribir "a", la lista se reduce a los elementos cuyo título contiene "a".

**Checkpoint 1.4 (final de la Parte 1):** la búsqueda filtra en tiempo real y al borrar el texto vuelven los 12 elementos.

---

## Parte 2 — Navegación con paso de argumento (2 horas aprox.)

### Paso 2.1 — Agrega la dependencia
**Qué hacer:** en `app/build.gradle.kts`:

```kotlin
implementation("androidx.navigation:navigation-compose:2.8.0")
```

Sincroniza Gradle.

**Checkpoint 2.1:** el IDE reconoce `NavHost` y `rememberNavController` sin errores.

### Paso 2.2 — Configura el `NavHost`
**Qué hacer:** crea `AppNavigation()` con dos rutas.

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                elementos = elementos,
                onElementoClick = { id -> navController.navigate("detalle/$id") }
            )
        }
        composable(
            route = "detalle/{elementoId}",
            arguments = listOf(navArgument("elementoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("elementoId") ?: 0
            val elemento = elementos.firstOrNull { it.id == id }
            if (elemento != null) {
                DetalleScreen(elemento = elemento, onBack = { navController.popBackStack() })
            }
        }
    }
}
```

**Checkpoint 2.2:** la app abre en la lista y no hay errores de compilación.

### Paso 2.3 — Conecta el click de cada tarjeta
**Qué hacer:** verifica que el `Modifier.clickable` de cada `Card` llame a `onElementoClick(elemento.id)`.

**Qué deberías ver:** al tocar la tarjeta 5, se abre el detalle del elemento 5 (no siempre el primero).

**Checkpoint 2.3:** tocar elementos distintos abre detalles distintos.

### Paso 2.4 — Construye la pantalla de detalle
**Qué hacer:** muestra toda la información del elemento.

```kotlin
@Composable
fun DetalleScreen(elemento: Elemento, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(elemento.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(elemento.categoria, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(16.dp))
        Text(elemento.descripcionLarga, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
```

**Checkpoint 2.4 (final de la Parte 2):** el botón "Volver" regresa a la lista, y la lista conserva el texto de búsqueda que había.

---

## Parte 3 — Cacería de bugs: navegación duplicada (1.5 horas aprox.)

### Paso 3.1 — Reproduce el bug
**Qué hacer:** toca la misma tarjeta varias veces **MUY rápido** (antes de que la pantalla de detalle termine de aparecer).

**Qué deberías ver:** múltiples pantallas de detalle apiladas. Al presionar "Volver", no regresas directo a la lista sino a otro detalle idéntico.

**Checkpoint 3.1:** documentaste el comportamiento incorrecto con captura de pantalla o descripción detallada.

### Paso 3.2 — Diagnostica
**Qué hacer:** explica por qué ocurre. Pista: cada llamada a `navigate()` **apila** un destino nuevo, sin verificar si ya está en el tope.

**Checkpoint 3.2:** escribiste en `ANALISIS.md` la causa técnica del bug.

### Paso 3.3 — Corrige
**Qué hacer:** usa las opciones de `navigate(...)` para evitar destinos duplicados en el tope de la pila.

```kotlin
navController.navigate("detalle/$id") {
    launchSingleTop = true
}
```

**Qué deberías ver:** tocar la misma tarjeta varias veces rápido ya NO apila pantallas duplicadas.

**Checkpoint 3.3 (final de la Parte 3):** tienes evidencia del bug antes y después de la corrección.

### Paso 3.4 — Verifica el caso límite
**Qué hacer:** prueba también tocar **dos tarjetas distintas** rápido. ¿Se comporta bien? ¿Debería?

**Checkpoint 3.4:** puedes explicar la diferencia entre "mismo destino repetido" (que `launchSingleTop` resuelve) y "destinos distintos en cadena" (que es navegación legítima).

---

## Parte 4 — Reto avanzado (bonus, +15 puntos, elige uno)

- **Opción A:** agrega filtro adicional por `categoria` (chips seleccionables) combinado con la búsqueda por texto.
- **Opción B:** conserva la posición de scroll de la lista al volver del detalle usando `rememberLazyListState()` elevado por encima de la navegación.
- **Opción C:** implementa una tercera ruta "favoritos" que muestre solo los elementos marcados como favoritos desde la lista principal, con su propio destino en el `NavHost`.

---

## Parte 5 — Pulido (30 min aprox.)

Agrega una imagen o ícono representativo en cada tarjeta y en el detalle, y verifica que la navegación hacia adelante y hacia atrás funcione correctamente varias veces seguidas, incluso con la búsqueda activa.

**Checkpoint 5:** navegas lista → detalle → volver → buscar → detalle → volver, cinco veces seguidas, sin comportamientos extraños.

---

## Entregables

- Código fuente completo del proyecto (repositorio Git o `.zip`).
- Capturas de pantalla de:
  - la lista con búsqueda activa,
  - un detalle,
  - evidencia del bug de navegación duplicada **antes** y **después** de la corrección.
- Archivo `ANALISIS.md` con la predicción inicial y la explicación técnica de la Parte 3.

---

## Rúbrica

| Criterio | Puntos |
|---|---|
| Lista funcional con `LazyColumn`, 12+ elementos y búsqueda en tiempo real | 25 |
| Navegación funcional lista → detalle con paso de argumento | 20 |
| Cacería de bugs: diagnóstico y corrección de la navegación duplicada | 30 |
| `ANALISIS.md`: calidad de la predicción y justificación técnica | 15 |
| Calidad general del código y pulido visual | 10 |
| Bonus — Parte 4 | +15 |

**Nota:** la cacería de bugs vale más (30) que la navegación funcional (20). El taller evalúa si **entiendes** la pila de navegación, no solo si lograste que algo se abriera.

---

## Lista de verificación antes de entregar

- [ ] La lista tiene 12+ elementos de al menos 3 categorías.
- [ ] La búsqueda filtra en tiempo real por título.
- [ ] `items` usa `key = { it.id }`.
- [ ] La navegación pasa el `id` y el detalle muestra el elemento correcto.
- [ ] El botón "Volver" usa `popBackStack()`.
- [ ] Reproduje el bug de navegación duplicada y lo documenté.
- [ ] Corregí el bug con `launchSingleTop = true` y verifiqué la corrección.
- [ ] `ANALISIS.md` tiene la predicción inicial y la explicación técnica.
- [ ] No hay `!!` en el código.

---

## Entrega

Sube un único archivo (`.zip` o enlace de repositorio) según la fecha indicada por el docente.
