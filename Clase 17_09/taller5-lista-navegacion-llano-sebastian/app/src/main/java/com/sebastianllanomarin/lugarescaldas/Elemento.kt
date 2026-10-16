package com.sebastianllanomarin.lugarescaldas

// Taller 5 - Sebastian Llano Marin - Codigo 55444
// Tema seleccionado: Lugares turísticos destacados del departamento de Caldas para visitar.

/**
 * Data class que representa un lugar turístico de Caldas.
 * Almacena toda la información relevante que se mostrará tanto en la lista principal
 * como en la vista de detalle.
 *
 * @property id Identificador único para cada elemento, utilizado como clave en las listas de Compose.
 * @property titulo Nombre del lugar turístico.
 * @property categoria Categoría a la que pertenece el lugar (ej. Pueblo, Naturaleza, Manizales).
 * @property descripcionCorta Texto breve que se muestra en las tarjetas de la lista.
 * @property descripcionLarga Texto extendido con detalles completos para la vista de detalle.
 */
data class Elemento(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcionCorta: String,
    val descripcionLarga: String
)

/**
 * Lógica modificada: Ahora el icono se genera usando las dos primeras letras de la categoría.
 * Esto permite una diferenciación visual un poco más específica, por ejemplo "PU" para Pueblo,
 * "NA" para Naturaleza, "MA" para Manizales.
 * Anteriormente solo se utilizaba la primera letra.
 */
fun iconoDe(categoria: String): String = categoria.take(2).uppercase()

// Colección inmutable de los 12 lugares turísticos, distribuidos equitativamente en 3 categorías.
val elementos = listOf(
    Elemento(
        1, "Salamina", "Pueblo",
        "Pueblo patrimonio de Colombia",
        "Salamina es conocida como la Ciudad Luz de Caldas. Tiene casas de bahareque con balcones tallados en madera y un parque principal muy bonito para caminar."
    ),
    Elemento(
        2, "Aguadas", "Pueblo",
        "Tierra del sombrero aguadeño",
        "Aguadas es pueblo patrimonio y es famoso por el sombrero aguadeño tejido en paja de iraca. También es sede del Festival Nacional del Pasillo."
    ),
    Elemento(
        3, "Riosucio", "Pueblo",
        "El pueblo del carnaval del diablo",
        "Riosucio es reconocido por el Carnaval de Riosucio, una fiesta llena de música, disfraces y tradición indígena, mestiza y afro."
    ),
    Elemento(
        4, "Chinchiná", "Pueblo",
        "Corazón cafetero de Caldas",
        "Chinchiná es uno de los municipios más cafeteros del departamento. Allí queda el centro de investigaciones del café, Cenicafé."
    ),
    Elemento(
        5, "Nevado del Ruiz", "Naturaleza",
        "Volcán nevado del parque Los Nevados",
        "El Nevado del Ruiz hace parte del Parque Nacional Natural Los Nevados. Se puede visitar con guía y se ven paisajes de páramo con frailejones."
    ),
    Elemento(
        6, "Termales del Otoño", "Naturaleza",
        "Aguas termales cerca de Manizales",
        "Los Termales del Otoño quedan en la vía al nevado. Son piscinas de agua caliente que viene del volcán, ideales para descansar en clima frío."
    ),
    Elemento(
        7, "Ecoparque Los Yarumos", "Naturaleza",
        "Bosque y senderos dentro de la ciudad",
        "Los Yarumos es un ecoparque en Manizales con senderos ecológicos, canopy y miradores desde donde se ve buena parte de la ciudad."
    ),
    Elemento(
        8, "Reserva Río Blanco", "Naturaleza",
        "Paraíso para ver aves",
        "La Reserva Río Blanco protege el agua que consume Manizales y es uno de los mejores lugares del país para el avistamiento de aves."
    ),
    Elemento(
        9, "Catedral Basílica", "Manizales",
        "La catedral más alta de Colombia",
        "La Catedral Basílica de Manizales tiene una torre a la que se puede subir por el Corredor Polaco y desde arriba se ve toda la ciudad."
    ),
    Elemento(
        10, "Torre de Chipre", "Manizales",
        "Mirador de los atardeceres",
        "En el barrio Chipre está la torre mirador y el Monumento a los Colonizadores. Es el mejor sitio para ver el atardecer en Manizales."
    ),
    Elemento(
        11, "Recinto del Pensamiento", "Manizales",
        "Mariposario y bosque de guadua",
        "El Recinto del Pensamiento tiene un mariposario, un jardín de orquídeas, un bosque de guaduas y un telesilla para recorrerlo."
    ),
    Elemento(
        12, "Hacienda Venecia", "Manizales",
        "Finca cafetera tradicional",
        "En la Hacienda Venecia se hace el tour del café, donde explican todo el proceso desde la semilla hasta la taza."
    )
)