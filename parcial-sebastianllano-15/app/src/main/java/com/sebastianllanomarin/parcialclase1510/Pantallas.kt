package com.sebastianllanomarin.parcialclase1510

// Parcial clase 15_10 - Sebastian Llano Marin - codigo 55444
// Pantallas de la app: lista con buscador/filtro y detalle del personaje

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun PantallaLista(
    modeloVista: ModeloVistaPersonajes,
    estadoLista: LazyGridState,
    alTocarElemento: (Int) -> Unit
) {
    val elementos by modeloVista.elementosFiltrados.collectAsStateWithLifecycle()
    val cargando by modeloVista.cargando.collectAsStateWithLifecycle()
    val verVillanos by modeloVista.verVillanos.collectAsStateWithLifecycle()
    val alcance = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Text(
            text = if (verVillanos) "Villanos · Sebastian Llano 55444" else "Superhéroes · Sebastian Llano 55444",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )

        ContenidoLista(
            elementos = elementos,
            cargando = cargando,
            verVillanos = verVillanos,
            estadoLista = estadoLista,
            alCambiarFiltro = { villanos ->
                modeloVista.mostrarVillanos(villanos)
                // Al cambiar de categoría se reinicia el scroll para no quedar a mitad de lista.
                // Se lanza en una corrutina porque scrollToItem es una función suspendida.
                alcance.launch { estadoLista.scrollToItem(0) }
            },
            alTocarElemento = alTocarElemento
        )
    }
}

@Composable
fun ContenidoLista(
    elementos: List<Elemento>,
    cargando: Boolean,
    verVillanos: Boolean,
    estadoLista: LazyGridState,
    alCambiarFiltro: (Boolean) -> Unit,
    alTocarElemento: (Int) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            FilterChip(
                selected = !verVillanos,
                onClick = { alCambiarFiltro(false) },
                label = { Text("Héroes") }
            )
            FilterChip(
                selected = verVillanos,
                onClick = { alCambiarFiltro(true) },
                label = { Text("Villanos") }
            )
        }

        if (cargando) {
            // Mientras carga se muestra un indicador centrado
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (elementos.isEmpty()) {
            // La búsqueda no coincidió con ningún nombre

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sin resultados")
            }
        } else {
            // LazyVerticalGrid solo compone los elementos visibles; key conserva la posición por id
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = estadoLista,
                modifier = Modifier.fillMaxSize()
            ) {
                items(elementos, key = { it.id }) { elemento ->
                    TarjetaElemento(
                        elemento = elemento,
                        alTocar = { alTocarElemento(elemento.id) }
                    )
                }
            }
        }
    }
}

// tarjeta con la imagen
@Composable
fun TarjetaElemento(
    elemento: Elemento,
    alTocar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { alTocar() },

        // tarjeta azul sobre el fondo negro
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        AsyncImage(
            model = elemento.imagenUrl,
            contentDescription = elemento.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Text(
            text = elemento.nombre,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PantallaDetalle(
    modeloVista: ModeloVistaPersonajes,
    id: Int,
    alVolver: () -> Unit
) {
    val elemento by remember(id) { modeloVista.elementoPorId(id) }
        .collectAsStateWithLifecycle(initialValue = null)

    val heroe = elemento ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        AsyncImage(
            model = heroe.imagenUrl,
            contentDescription = heroe.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Spacer(Modifier.height(16.dp))
        Text(heroe.nombre, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(heroe.descripcion, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Text("Superpoderes", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            heroe.superPoderes.forEach { poder ->
                AssistChip(onClick = {}, label = { Text(poder) })
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = alVolver) { Text("Volver") }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaLista() {
    MaterialTheme {
        ContenidoLista(
            elementos = obtenerSuperheroes().take(4),
            cargando = false,
            verVillanos = false,
            estadoLista = rememberLazyGridState(),
            alCambiarFiltro = {},
            alTocarElemento = {}
        )
    }
}
