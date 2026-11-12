# analisis taller 8 consumo api rick and morty
estudiante: sebastian llano
codigo: 55444

## 1. prediccion inicial antes de probar
pregunta del taller: si llamo a retrofit sin try catch desde viewmodelscope y quito internet antes de recargar que pasa?

mi prediccion:
si no tengo internet la peticion va a fallar porque no encuentra el servidor. como no hay try catch que atrape el error la excepcion sube a la corrutina en el viewmodel y como nadie la maneja se cae la app en el celular, o sea se cierra sola.

## 2. diferencia entre pokeapi y la de rick and morty
las dos devuelven un json con un array results pero en pokeapi cada item solo trae el name y una url para consultar aparte mientras que en rick and morty ya viene todo completo en el primer llamado como el id, name, status, species y la foto con image.

## 3. crash controlado y como se arreglo
excepcion en logcat cuando probe sin try catch:
`java.net.UnknownHostException: Unable to resolve host "rickandmortyapi.com": No address associated with hostname`

## por que pasa:
## okhttp y retrofit intentan resolver el dominio por dns y al no haber conexion se lanza unknownhostexception. como la funcion suspend no tenia try catch la corrutina se rompe y termina cerrando la aplicacion de golpe.

## como lo corregi en el repositorio:
## en personajesrepository meti la llamada dentro de try catch usando result:
```kotlin
suspend fun obtenerPersonajes(): Result<PersonajesResponse> =
    try {
        Result.success(api.obtenerPersonajes())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
```
## es importante volver a lanzar cancellationexception porque si la atrapo como un error normal las corrutinas no se cancelan bien cuando se cierra o cambia la pantalla.

## 4. diferencia ioexception y httpexception
- ioexception: es problema de conexion o red física, por ejemplo cuando no hay internet, falla el dns o se cae la señal y no alcanza ni a llegar al servidor.
- httpexception: si hubo conexion con el servidor pero este responde con error, tipo 404 no encontrado o 500 error interno.

## 5. por que se cargo el detalle por id
para la pantalla de detalle preferi llamar de nuevo al endpoint por id porque asi si mas adelante se usa un deep link o enlace directo la pantalla puede pedir sus propios datos sin depender de que la lista este guardada en memoria.
