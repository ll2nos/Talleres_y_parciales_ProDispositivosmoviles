# Análisis — Taller 4

**Estudiante:** Sebastian Llano · **Código:** 55444
**Proyecto:** PerfilUsuario (`com.ucaldas.perfil`)

## 1. Predicción inicial (escrita antes de programar)

1. **Si uso `remember { mutableStateOf(0) }` para un contador y roto la pantalla, ¿el valor se mantiene o se pierde?**
   Considero que el valor se perderá. Al cambiar la orientación, la `Activity` se destruye y se vuelve a crear. `remember` solo persiste el estado mientras el Composable permanece en el árbol de composición durante el ciclo de vida actual de la actividad.

2. **Si tengo una lista creada con `remember { mutableListOf(...) }` y le agrego un elemento con `.add()`, ¿la pantalla se actualiza?**
   Considero que no se actualizará, ya que a pesar de estar dentro de un bloque `remember`, la colección `mutableListOf` pertenece a la librería estándar de Kotlin y no notifica a Jetpack Compose sobre sus mutaciones para desencadenar una recomposición.

3. **¿Qué diferencia hay entre `remember` y `rememberSaveable`?**
   `remember` almacena objetos en la memoria de la composición actual y se pierden si ocurre una recreación de la Actividad (como en rotaciones de pantalla). En cambio, `rememberSaveable` serializa y almacena el estado utilizando el mecanismo de `Bundle` de Android (`onSaveInstanceState`), permitiendo que el estado sobreviva a la recreación de la Actividad o incluso a la terminación del proceso por el sistema.

## 2. Verificación de la predicción

- **Pregunta 1:** Acertado. Al incrementar el contador con `remember` y rotar la pantalla, el valor regresaba a su estado inicial.
- **Pregunta 2:** Acertado. La interfaz gráfica no reflejaba los nuevos elementos. La lista estándar no propaga notificaciones de cambio a Compose, siendo necesario utilizar colecciones observables como `mutableStateListOf`.
- **Pregunta 3:** Acertado. El uso de `rememberSaveable` demostró retener exitosamente tanto el texto ingresado como el contador durante la rotación.

## 3. Diagnóstico del bug (Parte 2)

### Síntoma
Al pulsar la opción "Añadir Logro", la interfaz no refleja la agregación del nuevo ítem en la lista visualizada, a pesar de ejecutar el código correspondiente.

### ¿Por qué ocurre?
La función `mutableListOf()` retorna un `ArrayList` estándar. El motor de Jetpack Compose depende de estructuras de datos observables (State) para saber cuándo es necesario invocar la recomposición. Un `ArrayList` estándar no está instrumentado para notificar al sistema Snapshot de Compose; por lo tanto, el framework ignora completamente que los datos internos han cambiado.

### Evidencia
Se implementó un trazado en Logcat (`Log.d`) para imprimir el tamaño de la lista al pulsar el botón. Los registros confirmaron que la lista incrementaba su tamaño (2, 3, 4 elementos), pero el nodo correspondiente a la lista en pantalla no se actualizaba.

### Corrección aplicada
Se sustituyó por `remember { mutableStateListOf("Primer commit exitoso") }`. Esto requiere un cambio mínimo en el código manteniendo el uso de funciones como `.add()`, pero soluciona el problema de raíz.

### ¿Por qué funciona?
`mutableStateListOf` provee una `SnapshotStateList`, un tipo de colección diseñada por Compose que emite notificaciones de cambio. Al insertar o remover un elemento, se marca el área de la interfaz afectada como "inválida", forzando a Compose a redibujarla para reflejar el estado actual.

## 4. Experimento de rotación (Parte 3)

### Con `remember`
El contador se inicializaba, se incrementaba, y al rotar el dispositivo volvía a cero porque su estado dependía directamente de la instancia de la Actividad, la cual era recreada.

### Con `rememberSaveable`
Al aplicar esta función, el contador retuvo su valor durante los cambios de configuración.

### Explicación técnica
El ciclo de vida de Android emplea un mecanismo basado en el objeto `Bundle` (mediante `onSaveInstanceState`). `rememberSaveable` abstrae este proceso, serializando los datos básicos dentro del bundle del sistema. Al reconstruir la interfaz, Compose lee el valor del estado restaurado en lugar del valor predeterminado proporcionado inicialmente.

*Nota técnica:* Para tipos de datos complejos o listas observables como `SnapshotStateList`, `rememberSaveable` requiere la implementación adicional de un `Saver` personalizado para indicar cómo transformar esa colección en tipos primitivos y viceversa, lo cual explica por qué los logros se restablecen por defecto tras una rotación.

## 5. Justificación de los Modifier usados

1. **Avatar: `.size(100.dp)` → `.clip(CircleShape)` → `.background(...)`**
   El sistema de modificadores de Compose se evalúa de manera secuencial (de arriba hacia abajo). Primero establecemos las dimensiones, luego aplicamos la máscara de recorte (clipping) circular y, finalmente, pintamos el fondo. Si el color se aplica antes del recorte, se dibuja un cuadrado.

2. **Column dentro de la Card: `.fillMaxWidth()` → `.padding(24.dp)`**
   El espaciado interno se aplica en el contenedor hijo (la `Column`) para asegurar que los márgenes, bordes y el color de fondo propios de la `Card` (Material Design) lleguen al extremo. Aplicar el `padding` en el propio modifier de la `Card` modificaría sus dimensiones externas y afectaría negativamente al diseño del sombreado.

3. **Contenedor Principal: `.fillMaxSize()` → `.safeDrawingPadding()` → `.verticalScroll(...)` → `.padding(16.dp)`**
   El modificador `.safeDrawingPadding()` es fundamental para los diseños de borde a borde (Edge-to-Edge), evitando colisiones visuales con la barra de navegación o de estado. Se incluye un `.verticalScroll()` para garantizar la accesibilidad completa del contenido independientemente de las dimensiones de la pantalla (especialmente crítico en vista horizontal).

## 6. Dificultades encontradas

- Comprender la abstracción del sistema Snapshot y asimilar que en Compose, "recordar" (`remember`) un objeto en memoria no implica que la interfaz reaccione a sus modificaciones internas.
- Adaptar el manejo del teclado en el `OutlinedTextField` mediante el `onValueChange` y sincronizarlo adecuadamente con el estado almacenado.
- Manejar layouts responsivos: al principio elementos clave de la interfaz quedaban ocultos durante la rotación debido a que no preveía la reducción de altura, haciendo obligatorio el uso del `rememberScrollState()`.
