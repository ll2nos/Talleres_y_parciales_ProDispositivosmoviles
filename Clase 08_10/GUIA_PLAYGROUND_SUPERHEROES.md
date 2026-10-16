# Guia playground: lista y detalle de superheroes con Jetpack Compose

Esta guia le permite al estudiante construir **exactamente el mismo ejercicio del proyecto** de forma guiada, entendiendo cada concepto mientras lo implementa. La meta es crear una app con:

1. Una **lista de superheroes**.
2. Una **card reutilizable** para cada elemento.
3. Una **navegacion** hacia una pantalla de detalle.
4. Una **pantalla de detalle enriquecida**, diferente a la card de la lista.

---

## 1. Resultado esperado

Al finalizar, la aplicacion debe permitir:

- Ver una lista vertical de superheroes.
- Pulsar una card para abrir el detalle.
- Mostrar una pantalla de detalle con imagen, nombre, descripcion, metricas y superpoderes.
- Volver a la lista con un boton.

---

## 2. Conceptos que se aprenderan

En este ejercicio el estudiante practicara:

- `data class` para modelar datos.
- `LazyColumn` para listas eficientes.
- `Card`, `Text`, `Button`, `Row`, `Column`, `Surface` y `FlowRow`.
- Reutilizacion de componentes en Compose.
- Parametros en funciones `@Composable`.
- Navegacion con `NavHost` y `NavController`.
- Renderizado condicional para usar una card simple o una vista de detalle.
- Separacion entre **datos**, **UI reusable** y **pantallas**.

---

## 3. Estructura mental del proyecto

Antes de escribir codigo, piense la app en tres capas:

| Capa | Que contiene | Ejemplo en este proyecto |
| --- | --- | --- |
| Datos | La informacion de los heroes | `Nombres.kt` |
| Navegacion y pantallas | Lista y detalle | `MainActivity.kt` |
| Componentes UI | Imagen, card, info y detalle | `UtilsUI.kt` |

Esta separacion es clave porque evita mezclar todo en un solo archivo.

---

## 4. Paso 1: crear el modelo de datos

Archivo: `app/src/main/java/com/ucaldas/aplicacion1/Nombres.kt`

### Objetivo

Crear una estructura que represente cada superheroe.

### Codigo base

```kotlin
data class Nombres(
    val id: Int,
    val nombre: String,
    val imagenUrl: String,
    val descripcion: String,
    val superPoderes: List<String>
)
```

### Que significa cada campo

- `id`: identifica de forma unica a cada heroe.
- `nombre`: texto principal que vera el usuario.
- `imagenUrl`: direccion de la imagen remota.
- `descripcion`: informacion corta del heroe.
- `superPoderes`: lista de habilidades.

### Idea importante

Una `data class` sirve para modelar informacion de forma clara. En Compose esto es muy util porque la UI recibe objetos bien definidos y los dibuja.

### Despues agregue una lista de prueba

```kotlin
val listaNombres = listOf(
    Nombres(
        1,
        "Superman",
        "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/lg/644-superman.jpg",
        "Superman es un heroe de Superman Prime One-Million reconocido por ACTION COMICS #1.",
        listOf("Inteligencia superior", "Super fuerza", "Super velocidad", "Alta resistencia")
    )
)
```

Luego puede seguir agregando mas heroes como en el proyecto.

---

## 5. Paso 2: crear una ruta para lista y detalle

Archivo: `app/src/main/java/com/ucaldas/aplicacion1/Utils.kt`

### Objetivo

Centralizar las rutas de navegacion para no escribir strings sueltos por todo el proyecto.

### Codigo

```kotlin
object Utils {
    object Rutas {
        const val LISTA = "lista"
        const val DETALLE = "detalle/{id}"

        fun detalle(id: Int) = "detalle/$id"
    }
}
```

### Que se entiende aqui

- `LISTA` representa la pantalla principal.
- `DETALLE` define una ruta con parametro.
- `detalle(id)` construye la ruta real, por ejemplo: `detalle/4`.

### Concepto clave

Esto hace la navegacion mas segura y mas facil de mantener.

---

## 6. Paso 3: crear el componente de imagen

Archivo: `app/src/main/java/com/ucaldas/aplicacion1/ui/theme/UtilsUI.kt`

### Objetivo

Encapsular la carga de imagen en un componente reutilizable.

### Codigo

```kotlin
@Composable
fun HeroImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        contentScale = ContentScale.Crop
    )
}
```

### Que aprende el estudiante

- `@Composable` define un bloque de interfaz.
- `modifier` permite reutilizar el componente con distintos tamanos o estilos.
- `AsyncImage` carga imagenes desde internet.
- `ContentScale.Crop` recorta la imagen para que se vea bien dentro del espacio disponible.

### Reflexion

Separar la imagen en su propia funcion evita repetir codigo dentro de cada card.

---

## 7. Paso 4: crear la informacion simple de la card

### Objetivo

Construir la informacion resumida que aparece en la lista.

### Codigo

```kotlin
@Composable
fun HeroInfo(
    hero: Nombres,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "ID: ${hero.id}",
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = hero.nombre,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = hero.descripcion,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Superpoderes: ${hero.superPoderes.joinToString()}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
```

### Que hace `Column`

Organiza los elementos en vertical.

### Que hace `joinToString()`

Convierte una lista de strings en un solo texto separado por comas.

### Diferencia pedagogica

Esta vista debe ser **compacta**, porque vive dentro de una lista.

---

## 8. Paso 5: crear una vista de detalle mas potente

### Objetivo

Hacer que el detalle no sea simplemente una copia de la card.

### Idea de diseno

La card de la lista debe responder rapido visualmente.  
La vista de detalle debe aprovechar mejor el espacio y contar una historia mas completa del heroe.

### Componentes usados

- `Surface` para bloques visuales destacados.
- `Row` y `Column` para estructura.
- `Box` para crear un avatar circular con la inicial.
- `FlowRow` para superpoderes tipo chips.
- `AssistChip` para mostrar habilidades individuales.

### Resultado conceptual

La pantalla de detalle incluye:

1. Encabezado fuerte con identificador, nombre e inicial.
2. Descripcion amplia.
3. Seccion de metricas.
4. Lista visual de superpoderes.
5. Resumen tactico.

### Ejemplo guiado

```kotlin
@Composable
fun HeroInfoDetail(
    hero: Nombres,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bloque principal destacado
        // Estadisticas del heroe
        // Chips de superpoderes
        // Resumen tactico
    }
}
```

### Que debe entender el estudiante

No siempre conviene reutilizar una misma UI para todo.  
En Compose, **reutilizar bien** no significa que todas las pantallas deban verse igual, sino que se construyan con piezas reutilizables.

---

## 9. Paso 6: crear metricas derivadas del heroe

### Objetivo

Mostrar informacion calculada a partir de los datos, no solo datos crudos.

### Ejemplo

```kotlin
private fun heroProfile(superPoderes: List<String>): String = when {
    superPoderes.any { it.contains("Inteligencia", ignoreCase = true) } &&
        superPoderes.any { it.contains("Combate", ignoreCase = true) } -> "Estratega"
    superPoderes.any { it.contains("Energia", ignoreCase = true) } -> "Cosmico"
    superPoderes.any { it.contains("velocidad", ignoreCase = true) } -> "Velocista"
    else -> "Titan"
}
```

### Que se aprende aqui

- `when` permite evaluar reglas.
- `any { ... }` revisa si algun elemento cumple una condicion.
- La UI puede mostrar resultados que nacen de una pequena logica de negocio.

### Valor pedagogico

Esto le ensena al estudiante que una pantalla puede ser **mas inteligente**, no solo mas bonita.

---

## 10. Paso 7: crear la card reutilizable

### Objetivo

Tener un componente central que funcione tanto en la lista como en el detalle.

### Codigo base

```kotlin
@Composable
fun HeroCard(
    hero: Nombres,
    onClick: (() -> Unit)? = null,
    option: Int = 0,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
    ) {
        HeroImage(
            imageUrl = hero.imagenUrl,
            contentDescription = hero.nombre
        )
        if (option == 0) {
            HeroInfo(hero = hero)
        } else {
            HeroInfoDetail(hero = hero)
        }
    }
}
```

### Como leer este componente

- Siempre muestra la imagen.
- Si `option == 0`, usa la vista simple.
- Si `option != 0`, usa la vista de detalle.
- Si recibe `onClick`, se vuelve interactivo.

### Concepto fuerte

Un solo componente puede tener **comportamientos distintos** segun sus parametros.

---

## 11. Paso 8: construir la lista principal

Archivo: `MainActivity.kt`

### Objetivo

Mostrar todos los heroes en una lista desplazable.

### Codigo

```kotlin
@Composable
fun ListaSuperHeroes(
    superheroes: List<Nombres>,
    onVerDetalle: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(superheroes, key = { it.id }) { heroe ->
            HeroCard(
                hero = heroe,
                onClick = { onVerDetalle(heroe.id) }
            )
        }
    }
}
```

### Por que `LazyColumn` y no `Column`

`LazyColumn` solo compone lo necesario en pantalla, por eso es ideal para listas.

### Que significa `key = { it.id }`

Le da una identidad estable a cada item. Esto mejora el manejo interno de la lista.

---

## 12. Paso 9: construir la pantalla de detalle

### Objetivo

Renderizar una vista completa de un heroe y permitir volver.

### Codigo

```kotlin
@Composable
fun DetalleSuperHeroe(
    hero: Nombres,
    onVolver: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeroCard(hero = hero, option = 1)
        }
        item {
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a la lista")
            }
        }
    }
}
```

### Que debe notar el estudiante

- El detalle reutiliza `HeroCard`.
- Pero al enviar `option = 1`, la informacion cambia.
- El boton permite volver sin reescribir logica compleja.

---

## 13. Paso 10: configurar la navegacion

### Objetivo

Conectar lista y detalle.

### Codigo guiado

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Utils.Rutas.LISTA
    ) {
        composable(Utils.Rutas.LISTA) {
            ListaSuperHeroes(
                superheroes = listaNombres,
                onVerDetalle = { id ->
                    navController.navigate(Utils.Rutas.detalle(id))
                }
            )
        }

        composable(
            route = Utils.Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            val hero = listaNombres.firstOrNull { it.id == id } ?: return@composable

            DetalleSuperHeroe(
                hero = hero,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
```

### Explicacion paso a paso

1. `rememberNavController()` crea el controlador de navegacion.
2. `NavHost` define las pantallas disponibles.
3. La ruta `lista` muestra todos los heroes.
4. Al pulsar una card, se navega usando el `id`.
5. La ruta `detalle/{id}` recibe el parametro.
6. Ese `id` se usa para buscar el heroe correcto.
7. Luego se renderiza la pantalla de detalle.

### Concepto clave

La navegacion no mueve pantallas fisicas como en XML clasico; en Compose, simplemente se decide **que composable se dibuja** segun la ruta activa.

---

## 14. Paso 11: conectar todo desde `MainActivity`

### Codigo final base

```kotlin
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aplicacion1Theme {
                AppNavigation()
            }
        }
    }
}
```

### Que pasa aqui

- `setContent` inicia Compose.
- `Aplicacion1Theme` aplica colores, tipografias y estilos globales.
- `AppNavigation()` toma el control de la experiencia.

---

## 15. Recorrido tecnico completo

Esta es la secuencia mental completa del ejercicio:

1. Defino el modelo `Nombres`.
2. Creo una lista `listaNombres`.
3. Creo componentes pequenos: imagen e informacion.
4. Construyo una `HeroCard`.
5. Creo una vista de detalle mejorada.
6. Muestro todos los heroes en `LazyColumn`.
7. Configuro las rutas de navegacion.
8. Uso el `id` para abrir el heroe correcto.
9. Regreso a la lista con `popBackStack()`.

---

## 16. Errores frecuentes del estudiante

### Error 1: usar `Column` en lugar de `LazyColumn`

Problema: toda la lista se intenta renderizar de una vez.  
Solucion: use `LazyColumn` para listas.

### Error 2: no pasar el `id` a la ruta

Problema: el detalle no sabe que heroe abrir.  
Solucion: navegar con `Utils.Rutas.detalle(heroe.id)`.

### Error 3: repetir codigo para lista y detalle

Problema: mucho codigo duplicado.  
Solucion: crear `HeroCard` y cambiar el comportamiento con parametros.

### Error 4: hacer que detalle y lista se vean igual

Problema: el detalle no aporta valor.  
Solucion: en detalle use mas componentes, jerarquia visual y logica derivada.

### Error 5: mezclar datos, UI y navegacion en una sola funcion

Problema: codigo dificil de entender y mantener.  
Solucion: separar archivos por responsabilidad.

---

## 17. Mini laboratorio de comprobacion

El estudiante debe revisar si logro esto:

- [ ] La app abre en la lista.
- [ ] Cada heroe aparece dentro de una card.
- [ ] Al tocar una card se abre el detalle correcto.
- [ ] El detalle tiene una presentacion distinta a la lista.
- [ ] Se muestran los superpoderes del heroe.
- [ ] El boton de volver funciona.

Si una de estas casillas falla, el ejercicio aun no esta completo.

---

## 18. Retos extra para reforzar aprendizaje

Cuando el estudiante termine, puede extender el ejercicio con estos retos:

1. Agregar un campo `universo` con valores como `Marvel` o `DC`.
2. Colorear la card diferente segun el universo.
3. Agregar un buscador por nombre.
4. Mostrar un contador total de heroes en la parte superior.
5. Agregar una pantalla de favoritos.
6. Cambiar `option: Int` por algo mas expresivo, como un enum o un boolean mas claro.

---

## 19. Preguntas de reflexion para clase

1. Por que `LazyColumn` es mejor que `Column` para listas largas?
2. Que ventaja tiene encapsular la imagen en `HeroImage`?
3. Por que el detalle no deberia ser una copia exacta de la card?
4. Que problema resuelve pasar `id` en la ruta?
5. En que momento una UI deja de ser solo visual y empieza a expresar logica?

---

## 20. Cierre pedagogico

Este ejercicio no solo ensena a dibujar componentes en Compose.  
Tambien ensena a pensar una app en piezas:

- datos claros,
- componentes reutilizables,
- navegacion controlada,
- y pantallas con distintas responsabilidades.

La leccion mas importante es esta:

> una buena interfaz no se construye copiando bloques, sino diseñando componentes reutilizables que cambian de acuerdo con el contexto.

Con este proyecto, el estudiante ya practica una base muy real de desarrollo Android moderno con Jetpack Compose.
