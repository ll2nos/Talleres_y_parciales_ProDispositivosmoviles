# Guía del Estudiante — Clase 06: Navegación y Listas
## Programación para Dispositivos Móviles — Universidad de Caldas
**Facultad de Ingeniería** · Unidad 2: Interfaces de Usuario · Semana 6 · Duración: 3 horas

---

## ¿Qué voy a aprender?

Hasta ahora sabes construir **una** pantalla. Hoy aprendes a construir **muchas** y a conectarlas:

1. Cómo mostrar listas de datos eficientemente con `LazyColumn` (el equivalente en Compose de `RecyclerView`).
2. Por qué `RecyclerView` reutilizaba vistas (patrón ViewHolder) y por qué `LazyColumn` resuelve el mismo problema de forma declarativa.
3. Cómo navegar entre pantallas dentro de una sola Activity usando Navigation Compose (`NavHost`, `NavController`).
4. Cómo pasar datos (por ejemplo, un id) de una pantalla de lista a una pantalla de detalle.

**Por qué importa:** casi toda app real es una lista que lleva a un detalle. Esa estructura es exactamente la del proyecto final.

---

## Antes de la clase

- Trae funcionando la pantalla de perfil de la Clase 5.
- Piensa en una lista de datos simple (superhéroes, productos, películas) que te gustaría mostrar.

### Autochequeo de entrada
1. ¿Qué hace `remember` en Compose? (Clase 5)
2. ¿Qué diferencia hay entre `remember` y `rememberSaveable`? (Clase 5)
3. ¿Qué es una `data class`? (Clase 3)

---

## Durante la clase

### Bloque 1 — `LazyColumn` y `items`

#### 1.1 El modelo y los datos

```kotlin
data class Superheroe(val id: Int, val nombre: String, val poder: String)

val listaHeroes = listOf(
    Superheroe(1, "Spider-Man", "Sentido arácnido"),
    Superheroe(2, "Iron Man", "Traje de combate"),
    Superheroe(3, "Thor", "Control del rayo")
)
```

#### 1.2 La lista

```kotlin
@Composable
fun ListaHeroesScreen(heroes: List<Superheroe>) {
    LazyColumn {
        items(heroes) { heroe ->
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = heroe.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = heroe.poder, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
```

**Punto clave:** `LazyColumn` solo compone (crea) los elementos **visibles en pantalla**, no los 1000 elementos de la lista completa.

#### 1.3 El contraste que debes recordar

```kotlin
// ❌ Anti-patrón: compone los 10.000 elementos de una vez
Column {
    heroes.forEach { heroe ->
        Card { Text(heroe.nombre) }
    }
}
```

Con 10.000 elementos, `Column` + `forEach` congela la app. `LazyColumn` es fluido. **Esa es la diferencia entre "funciona" y "funciona bien".**

#### 1.4 `items` vs `item`

```kotlin
LazyColumn {
    item { Text("Encabezado") }          // un solo elemento
    items(heroes) { heroe -> ... }        // una lista de elementos
    item { Text("Pie de página") }        // otro elemento suelto
}
```

#### 1.5 `key` — el parámetro que casi nadie usa y todos deberían

```kotlin
LazyColumn {
    items(heroes, key = { it.id }) { heroe ->
        Card { Text(heroe.nombre) }
    }
}
```

Sin `key`, Compose identifica los elementos por **posición**. Si la lista se reordena o se filtra, Compose puede reutilizar el estado equivocado. Con `key`, identifica cada elemento por su identidad.

**Esto será crítico cuando agregues búsqueda en el taller.**

---

### Playground 1 — Tu primera lista (15 min)

**Tu turno.** Construye una `LazyColumn` con al menos 8 elementos de un tema libre (películas, libros, productos, canciones). Cada tarjeta debe mostrar un título y un subtítulo.

**Checkpoint 1:** la lista se desplaza suavemente y cada tarjeta muestra los datos correctos.

**Errores frecuentes:**
- Olvidar `import androidx.compose.foundation.lazy.items`.
- Usar `Column` + `forEach` por costumbre.
- Olvidar `Modifier.fillMaxWidth()` y ver tarjetas del ancho del texto.

---

### Bloque 2 — Referencia histórica: `RecyclerView`

**Este bloque es de lectura, no de codificación.**

```kotlin
// Código de REFERENCIA para entender qué resolvía RecyclerView (no se implementa en este curso)
class HeroeAdapter(val heroes: List<Superheroe>) : RecyclerView.Adapter<HeroeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) { /* referencias a TextViews */ }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { /* infla XML */ }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { /* asigna datos a las vistas */ }
    override fun getItemCount() = heroes.size
}
```

| Pieza | Qué hacía | Equivalente en Compose |
|---|---|---|
| `Adapter` | Decía qué dato va en qué posición | `items(heroes) { ... }` |
| `ViewHolder` | Reutilizaba las vistas para no crear una nueva por fila | Automático en `LazyColumn` |
| `onCreateViewHolder` | Inflaba el XML de la fila | Automático |
| `onBindViewHolder` | Asignaba datos a las vistas | El cuerpo de la lambda de `items` |
| `getItemCount` | Cuántos elementos hay | `heroes.size` (implícito) |
| `notifyDataSetChanged` | Avisaba que los datos cambiaron | Automático (recomposición) |

**Lo que debes entender:** `LazyColumn` + `items` hacen esto mismo internamente sin que escribas ese código repetitivo. **No es que Compose sea "más fácil": es que el trabajo se movió al framework.**

---

### Bloque 3 — Navigation Compose

#### 3.1 La dependencia

```kotlin
// build.gradle.kts (Module :app)
implementation("androidx.navigation:navigation-compose:2.8.0")
```

Después de agregarla, **sincroniza Gradle**. Si no ves las clases `NavHost`/`rememberNavController`, casi siempre es porque no sincronizaste.

#### 3.2 El `NavHost` completo

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaHeroesScreen(
                heroes = listaHeroes,
                onHeroeClick = { id -> navController.navigate("detalle/$id") }
            )
        }
        composable(
            route = "detalle/{heroeId}",
            arguments = listOf(navArgument("heroeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("heroeId") ?: 0
            val heroe = listaHeroes.first { it.id == id }
            DetalleHeroeScreen(heroe = heroe, onBack = { navController.popBackStack() })
        }
    }
}
```

**Los cuatro conceptos:**
1. **`rememberNavController()`** — controla la navegación. **Debe ir dentro de `remember`**, si no, se crea uno nuevo en cada recomposición y la navegación se rompe.
2. **`NavHost`** — el contenedor que declara las rutas disponibles.
3. **`startDestination`** — la ruta inicial.
4. **`composable("ruta") { }`** — declara una pantalla.

#### 3.3 Las pantallas

```kotlin
@Composable
fun ListaHeroesScreen(heroes: List<Superheroe>, onHeroeClick: (Int) -> Unit) {
    LazyColumn {
        items(heroes, key = { it.id }) { heroe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onHeroeClick(heroe.id) }
            ) {
                Text(text = heroe.nombre, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun DetalleHeroeScreen(heroe: Superheroe, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = heroe.nombre, style = MaterialTheme.typography.headlineMedium)
        Text(text = heroe.poder, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
```

**Patrón arquitectónico clave:** las pantallas **no conocen** al `NavController`. Reciben lambdas (`onHeroeClick`, `onBack`). Esto las hace reutilizables y testeables.

#### 3.4 El bug que vas a ver en la demo

Toca la misma tarjeta tres veces rápido. Muy probablemente verás **tres detalles apilados**. La corrección:

```kotlin
navController.navigate("detalle/$id") {
    launchSingleTop = true
}
```

**Qué hace:** si el destino ya está en el tope de la pila, no lo apila de nuevo. Es el tema de la Parte 3 del taller.

---

### Playground 2 — Lista → detalle (25 min)

**Tu turno.** Conecta tu lista del Playground 1 con una pantalla de detalle.

1. Agrega `navigation-compose` a `build.gradle.kts`.
2. Crea `AppNavigation()` con `NavHost` y dos rutas: `"lista"` y `"detalle/{id}"`.
3. Pasa el `id` del elemento tocado.
4. En el detalle, recupera el `id` y muestra la información completa.
5. Agrega el botón "Volver" con `popBackStack()`.

**Checkpoint 2:** tocar cualquier elemento navega al detalle **correcto** (no siempre al primero). Si siempre muestra el mismo, el argumento no se está pasando bien.

**Errores frecuentes:**
- Olvidar `NavType.IntType` → error de parsing al recuperar el argumento.
- Crear el `NavController` fuera de `remember` → la navegación se rompe.
- Usar `first { }` sin manejar el caso de que no exista → crash. Usa `firstOrNull()`.

---

## Después de la clase (trabajo independiente — ~6 horas)

Completa el **`Taller_Clase06.md`** (Taller 5 — Reto: Lista con Navegación, Búsqueda y una Pila que No Debe Explotar).

### Distribución sugerida de las 6 horas

| Parte | Contenido | Tiempo |
|---|---|---|
| Predicción | ¿Cuántas pantallas se apilan si toco 3 veces rápido? | 10 min |
| Parte 1 | Datos y lista con filtro de búsqueda | 2 h |
| Parte 2 | Navegación con paso de argumento | 2 h |
| Parte 3 | Cacería de bugs: navegación duplicada | 1.5 h |
| Parte 4 | Bonus: filtro por categoría / scroll preservado / favoritos | opcional |
| Parte 5 | Pulido visual | 30 min |

**Observa el peso de la rúbrica:** la cacería de bugs vale más (30) que la navegación funcional (20). El taller evalúa si **entiendes** la pila de navegación, no solo si lograste que algo se abriera.

---

## Conceptos clave para repasar

| Concepto | En una frase |
|---|---|
| `LazyColumn` | Lista vertical que solo compone los elementos visibles en pantalla (eficiente con muchos datos). |
| `items(lista) { ... }` | Describe cómo se ve cada elemento de la lista. |
| `item { ... }` | Un elemento suelto dentro de la lista (encabezado, pie de página). |
| `key` | Identificador estable de cada elemento; evita que Compose confunda posiciones al filtrar o reordenar. |
| `RecyclerView` (referencia histórica) | Sistema de vistas que reutilizaba filas con el patrón ViewHolder; hoy Compose resuelve esto internamente. |
| `NavController` | Objeto que controla a qué pantalla navegar dentro del `NavHost`. |
| `NavHost` | Contenedor que declara las rutas ("pantallas") disponibles de la app. |
| `composable("ruta") { }` | Declara una pantalla asociada a una ruta. |
| `navArgument` | Declara el tipo de un argumento de ruta (por ejemplo, `NavType.IntType`). |
| `popBackStack()` | Regresa a la pantalla anterior en la pila. |
| `launchSingleTop` | Evita apilar el mismo destino dos veces seguidas. |
| Pila de retroceso | Estructura LIFO de pantallas visitadas; "Atrás" desapila. |

---

## Autoevaluación

Responde sin mirar. Si fallas 3 o más, vuelve al bloque correspondiente.

1. ¿Por qué `LazyColumn` es más eficiente que `Column` + `forEach`?
2. ¿Qué hacía `onBindViewHolder` en un `RecyclerView` y cuál es su equivalente en Compose?
3. ¿Para qué sirve el parámetro `key` en `items`?
4. ¿Por qué `rememberNavController()` debe ir dentro de `remember`?
5. ¿Qué diferencia hay entre `NavHost` y `NavController`?
6. ¿Cómo se declara que un argumento de ruta es un `Int`?
7. ¿Qué hace `popBackStack()`?
8. ¿Qué problema resuelve `launchSingleTop = true`?
9. ¿Por qué las pantallas reciben lambdas (`onHeroeClick`) en lugar del `NavController`?
10. ¿Qué pasa si usas `first { }` y el id no existe?

<details>
<summary>Respuestas</summary>

1. Porque solo compone los elementos visibles; `Column` compone todos de una vez.
2. Asignaba los datos a las vistas de una fila reutilizada. Equivalente: el cuerpo de la lambda de `items`.
3. Da identidad estable a cada elemento, para que Compose no confunda posiciones al filtrar o reordenar.
4. Porque si se crea fuera, se genera un `NavController` nuevo en cada recomposición y la navegación se rompe.
5. `NavHost` es el contenedor que declara las rutas; `NavController` es el objeto que ejecuta la navegación.
6. `arguments = listOf(navArgument("id") { type = NavType.IntType })`.
7. Regresa a la pantalla anterior en la pila de retroceso.
8. Evita que el mismo destino se apile dos veces seguidas (navegación duplicada).
9. Para que las pantallas no dependan del `NavController`: son reutilizables y testeables.
10. Lanza `NoSuchElementException`. Usa `firstOrNull()`.
</details>

---

## Solución de problemas (troubleshooting)

| Síntoma | Causa probable | Solución |
|---|---|---|
| `Unresolved reference: items` | Falta el import | `import androidx.compose.foundation.lazy.items` |
| La app se congela con listas largas | Usaste `Column` + `forEach` | Cambia a `LazyColumn` + `items` |
| Error de parsing al recuperar el argumento | Falta `NavType.IntType` | Declara el tipo en `navArgument` |
| La navegación se rompe tras recomponer | `NavController` creado fuera de `remember` | Usa `rememberNavController()` |
| Siempre muestra el mismo detalle | El argumento no se está pasando | Revisa la ruta `detalle/$id` y la lectura de `heroeId` |
| Pantallas de detalle apiladas | Navegación duplicada | `launchSingleTop = true` |
| `NoSuchElementException` | `first { }` con un id inexistente | Usa `firstOrNull()` |
| La lista se ve mal al filtrar | Falta `key` en `items` | Agrega `key = { it.id }` |
| No compila tras agregar la dependencia | Gradle no sincronizado | Sync Now |
| El botón "Volver" no hace nada | `popBackStack()` no conectado al `onClick` | Revisa que el `Button` reciba `onClick = onBack` |

---

## Glosario

- **Lazy:** que se evalúa o crea solo cuando se necesita. `LazyColumn` crea los elementos a medida que se necesitan.
- **Composición:** el proceso por el cual Compose construye la descripción de la UI.
- **Recomposición:** volver a ejecutar un `@Composable` cuando su estado cambia.
- **ViewHolder:** patrón del sistema de Views que reutilizaba las vistas de las filas para no crearlas de nuevo.
- **Adapter:** clase del sistema de Views que conectaba los datos con las vistas de una lista.
- **Ruta (route):** identificador de una pantalla dentro del `NavHost`.
- **Pila de retroceso (back stack):** estructura LIFO de pantallas visitadas.
- **Argumento de navegación:** dato que se pasa de una pantalla a otra a través de la ruta.
- **`launchSingleTop`:** opción de navegación que evita apilar el mismo destino dos veces seguidas.
- **`key`:** identificador estable de un elemento de lista, para que Compose lo reconozca entre recomposiciones.

---

## Enlace con la siguiente clase

La **Clase 07 es el Primer Parcial**: evalúa Kotlin fundamentals y UI básica (Compose, estado, listas y navegación). Todo lo de las Clases 1 a 6 entra.

**Lo que debes repasar para el parcial:**
- Kotlin: `val`/`var`, nulabilidad, `data class`, `sealed class`, lambdas, extension functions.
- Compose: `@Composable`, `Column`/`Row`/`Box`, `Modifier`, `remember`, `rememberSaveable`, estado y recomposición.
- Listas: `LazyColumn`, `items`, `key`.
- Navegación: `NavHost`, `NavController`, rutas, argumentos, `popBackStack`.

Y en el **proyecto final** (5 APIs, ver `_Material de Curso (Programa)/Proyecto_Final_5_APIs.md`): cada una de las 5 rutas tendrá exactamente este patrón — **una lista que navega a un detalle pasando un id**.
