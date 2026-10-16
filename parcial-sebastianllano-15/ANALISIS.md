# Análisis de limpieza de identidad

Proyecto: `parcial-clase15_10-sebastianllano`
Autor actual: **Sebastian Llano Marin** · Código **55444**

## Cambios realizados
| Elemento | Antes | Ahora |
|---|---|---|
| Paquete / carpeta | `com.diegoivanrojasavila` | `com.sebastianllanomarin` |
| `namespace` y `applicationId` (build.gradle.kts) | `com.diegoivanrojasavila.parcialclase1510` | `com.sebastianllanomarin.parcialclase1510` |
| `app_name` (strings.xml) | Parcial 15_10 Diego Rojas | Parcial 15_10 Sebastian Llano Marin 55444 |
| Encabezados de los 5 `.kt` | diego rojas 35295 | Sebastian Llano Marin - codigo 55444 |
| Títulos en pantalla | … Diego Rojas 35295 | … Sebastian Llano 55444 |

## Cambios de lógica
- **ModeloVistaPersonajes**: ahora usa un único `EstadoPantalla` inmutable
  (`MutableStateFlow` + `update`) y flujos derivados con `map`.
- La búsqueda ignora espacios sobrantes y la lista se ordena A-Z.
- **Pantallas**: mensaje "Sin resultados" cuando el filtro no coincide.
- **ActividadPrincipal**: rutas y argumento de navegación como constantes.
- Comentarios ampliados (KDoc y explicaciones de cada decisión).

## Verificación
Búsqueda sin distinguir mayúsculas de `diego`, `rojas`, `avila` y `35295`
en todos los archivos y nombres de carpeta del proyecto: **0 coincidencias**.

> [!NOTE]
> La carpeta contenedora externa del workspace
> (`Diego_Rojas_35295_Dispositivos_Moviles`) está fuera del proyecto;
> hay que renombrarla manualmente desde el Explorador de Windows
> con el IDE cerrado, porque está en uso.
