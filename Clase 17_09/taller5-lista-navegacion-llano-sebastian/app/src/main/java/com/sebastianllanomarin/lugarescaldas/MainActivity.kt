package com.sebastianllanomarin.lugarescaldas

// Taller 5 - Sebastian Llano Marin - Codigo 55444

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private val EsquemaCaldas = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF6D4C41),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEFEBE9),
    onSecondaryContainer = Color(0xFF3E2723),
    background = Color(0xFFF7F3EC),
    onBackground = Color(0xFF2B2118),
    surface = Color(0xFFF7F3EC),
    onSurface = Color(0xFF2B2118),
    surfaceVariant = Color(0xFFE8F0E3),
    onSurfaceVariant = Color(0xFF4E5B45),
    outline = Color(0xFF8D6E63)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = EsquemaCaldas) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // State hoisting: el scroll vive fuera del NavHost para no perder la posición al volver del detalle.
    val listState = rememberLazyListState()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                elementos = elementos,
                listState = listState,
                onElementoClick = { id ->
                    // launchSingleTop evita abrir el detalle duplicado con toques rápidos.
                    navController.navigate("detalle/$id") {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(
            route = "detalle/{elementoId}",
            arguments = listOf(navArgument("elementoId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Elvis (?:) en lugar de !! para evitar crash si el argumento es nulo.
            val id = backStackEntry.arguments?.getInt("elementoId") ?: 0
            val elemento = elementos.firstOrNull { it.id == id }

            if (elemento != null) {
                DetalleScreen(elemento = elemento, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun ListaScreen(
    elementos: List<Elemento>,
    listState: LazyListState,
    onElementoClick: (Int) -> Unit
) {
    // rememberSaveable conserva la búsqueda al rotar o al navegar al detalle.
    var query by rememberSaveable { mutableStateOf("") }

    // Se recalcula solo cuando cambia 'query'. Cambio de lógica: filtra por título y categoría.
    val filtrados = remember(query) {
        if (query.isBlank()) {
            elementos
        } else {
            elementos.filter { 
                it.titulo.contains(query, ignoreCase = true) || 
                it.categoria.contains(query, ignoreCase = true) 
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Text(
            text = "Lugares de Caldas · Sebastian Llano Marin 55444",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar por nombre o categoría") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        LazyColumn(state = listState) {
            // 'key' estable para que Compose identifique cada fila al filtrar.
            items(filtrados, key = { it.id }) { elemento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onElementoClick(elemento.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icono(categoria = elemento.categoria, tamano = 48)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(elemento.titulo, style = MaterialTheme.typography.titleMedium)
                            Text(elemento.descripcionCorta, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleScreen(elemento: Elemento, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        Icono(categoria = elemento.categoria, tamano = 80)
        Spacer(Modifier.height(16.dp))
        Text(elemento.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(elemento.categoria, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(16.dp))
        Text(elemento.descripcionLarga, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver a la lista") }
    }
}

@Composable
fun Icono(categoria: String, tamano: Int) {
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iconoDe(categoria),
            // tamano / 2.5 para que quepan las 2 letras.
            fontSize = (tamano / 2.5).sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListaPreview() {
    MaterialTheme(colorScheme = EsquemaCaldas) {
        ListaScreen(elementos = elementos, listState = rememberLazyListState(), onElementoClick = {})
    }
}