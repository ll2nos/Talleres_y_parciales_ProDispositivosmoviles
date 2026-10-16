package com.sebastianllanomarin.parcialclase1510

// Parcial clase 15_10 - Sebastian Llano Marin - codigo 55444
// Punto de entrada de la app y grafo de navegación (lista -> detalle).

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Paleta de la app: fondo negro y acentos azules
private val EsquemaNegroAzul = darkColorScheme(
    primary = Color(0xFF1E6FFF),            // azul principal (tarjetas, botones)
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0A3D91),   // azul oscuro tras las imágenes
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF1E6FFF), // chips seleccionados y superpoderes
    onSecondaryContainer = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

// Nombres de las rutas centralizados para evitar errores de escritura
private const val RUTA_LISTA = "lista"
private const val ARGUMENTO_ID = "elementoId"
private const val RUTA_DETALLE = "detalle/{$ARGUMENTO_ID}"

class ActividadPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Compose dibuja toda la interfaz dentro de setContent
        setContent {
            MaterialTheme(colorScheme = EsquemaNegroAzul) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavegacionPrincipal()
                }
            }
        }
    }
}

@Composable
fun NavegacionPrincipal() {
    val navegador = rememberNavController()

    // Un único modelo de vista compartido por la lista y el detalle,
    // así ambos ven los mismos datos y el filtro no se pierde al navegar.
    val modeloVista: ModeloVistaPersonajes = viewModel()

    val posicionLista = rememberLazyGridState()

    NavHost(navController = navegador, startDestination = RUTA_LISTA) {
        composable(RUTA_LISTA) {
            PantallaLista(
                modeloVista = modeloVista,
                estadoLista = posicionLista,
                alTocarElemento = { id ->
                    // launchSingleTop evita abrir dos detalles por doble toque
                    navegador.navigate("detalle/$id") { launchSingleTop = true }
                }
            )
        }
        composable(
            route = RUTA_DETALLE,
            arguments = listOf(navArgument(ARGUMENTO_ID) { type = NavType.IntType })
        ) { entrada ->


            // Si por algún motivo falta el id se usa 0 (no existe, no muestra nada)
            val id = entrada.arguments?.getInt(ARGUMENTO_ID) ?: 0
            PantallaDetalle(
                modeloVista = modeloVista,
                id = id,
                alVolver = { navegador.popBackStack() }
            )
        }
    }
}
