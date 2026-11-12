package com.example.consumir_api.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.consumir_api.viewmodel.DetalleUiState
import com.example.consumir_api.viewmodel.PersonajesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePersonajeScreen(
    id: Int,
    onNavigateBack: () -> Unit,
    viewModel: PersonajesViewModel = viewModel()
) {
    val detalleState by viewModel.detalleState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.cargarDetalle(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Personaje") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("< Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = detalleState) {
                is DetalleUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is DetalleUiState.Success -> {
                    val personaje = state.personaje
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage(
                            model = personaje.imagenUrl,
                            contentDescription = "Imagen de ${personaje.name}",
                            modifier = Modifier.size(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = personaje.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Estado: ${personaje.status}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Especie: ${personaje.species}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                is DetalleUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarDetalle(id) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}
