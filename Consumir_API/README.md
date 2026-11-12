# Rick & Morty App - Taller 8

Este proyecto es la solución al Taller 8 de Programación para Dispositivos Móviles, en el cual se ha migrado de la PokéAPI a la API de Rick & Morty aplicando buenas prácticas en Android con Jetpack Compose.

## ¿Qué se ha implementado?

Se han realizado los siguientes cambios y adiciones al proyecto:

### 1. Configuración de Dependencias y Permisos
- Se añadió el permiso de Internet en el `AndroidManifest.xml` (`<uses-permission android:name="android.permission.INTERNET" />`).
- Se agregaron las dependencias en `build.gradle.kts` para:
  - **Retrofit & Gson**: Para consumir la API y parsear el JSON.
  - **Lifecycle & ViewModel Compose**: Para manejar el estado usando `collectAsStateWithLifecycle`.
  - **Navigation Compose**: Para movernos entre la lista de personajes y la vista de detalle.
  - **Coil**: Para mostrar las imágenes que provienen de la API mediante `AsyncImage`.

### 2. Capa de Modelos (Data)
Ubicados en el paquete `model`:
- **`Personaje`**: Representa un personaje de Rick & Morty. Contiene `id`, `name`, `status`, `species` e `imagenUrl` (Mapeada desde `image` usando la anotación `@SerializedName("image")`).
- **`PersonajesResponse`**: Contiene la lista de personajes (encapsulados dentro del arreglo `results`), modelando fielmente la estructura del JSON de la API.

### 3. Capa de Red (Network)
Ubicados en el paquete `network`:
- **`RickMortyApi`**: Una interfaz que define los *endpoints* a consumir mediante Retrofit (`@GET("character")` y `@GET("character/{id}")`).
- **`RetrofitInstance`**: Objeto Singleton que inicializa el cliente Retrofit con la URL base (`https://rickandmortyapi.com/api/`) y el convertidor de Gson.

### 4. Capa de Datos (Repository)
Ubicado en el paquete `repository`:
- **`PersonajesRepository`**: Se encarga de hacer las llamadas a la red. Incorpora un bloque `try/catch` muy importante que retorna un objeto `Result`. Esto previene que la app crashee cuando no hay conexión. Además, se re-lanza de forma segura la excepción `CancellationException` para no interferir con el ciclo de vida normal de las corrutinas.

### 5. Capa de Presentación (ViewModel y UiState)
Ubicado en el paquete `viewmodel`:
- **`UiState` y `DetalleUiState`**: Clases selladas (`sealed interface`) que representan los 3 estados posibles de las pantallas: `Loading` (Cargando), `Success` (Éxito) y `Error`.
- **`PersonajesViewModel`**: Coordina la obtención de datos a través del repositorio y expone su estado como `StateFlow`. Aquí diferenciamos entre errores de conexión (`IOException`) y del servidor (`HttpException`) para proveer un mensaje adecuado al usuario.

### 6. Interfaz de Usuario (UI) con Compose
Ubicadas en `MainActivity.kt` y el paquete `ui/screens`:
- **`MainActivity`**: Define el `NavHost` de Navigation Compose con dos rutas principales: `lista` y `detalle/{id}`.
- **`ListaPersonajesScreen`**: Muestra una `LazyColumn` con los personajes recibidos. Reacciona a los 3 estados:
  - En `Loading`: Muestra un spinner (`CircularProgressIndicator`).
  - En `Success`: Muestra cada elemento usando un `Card` con el nombre y estado.
  - En `Error`: Muestra un mensaje y un botón de "Reintentar".
- **`DetallePersonajeScreen`**: Al recibir el ID desde la navegación, le pide al ViewModel cargar el personaje específico. Luego muestra una pantalla con nombre, estado, especie y la fotografía del personaje haciendo uso de Coil (`AsyncImage`).

## Notas Adicionales
Consulta el archivo `ANALISIS.md` donde se discuten a profundidad:
- El comportamiento de la app ante fallas de red sin `try/catch`.
- La justificación del diseño elegido para el detalle del personaje.
- La diferencia fundamental entre un `IOException` y un `HttpException`.
