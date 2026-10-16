package com.ucaldas.perfil

/**
 * Taller 4 - Interfaz de Usuario con Jetpack Compose
 * Autor: Sebastian Llano
 * Código: 55444
 * 
 * Este archivo contiene la implementación de una pantalla de perfil de usuario
 * utilizando Jetpack Compose. Demuestra el manejo de estado, modificadores,
 * componentes de diseño y animaciones básicas.
 */

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.material3.lightColorScheme

// Paleta zapote con blanco
private val Zapote = Color(0xFFE8703A)
private val ZapoteClaro = Color(0xFFFFDCCB)
private val ZapoteOscuro = Color(0xFF7A2E0E)

private val EsquemaZapote = lightColorScheme(
    primary = Zapote,
    onPrimary = Color.White,
    primaryContainer = ZapoteClaro,
    onPrimaryContainer = ZapoteOscuro,
    secondary = Zapote,
    onSecondary = Color.White,
    secondaryContainer = Zapote,
    onSecondaryContainer = Color.White,
    background = Color.White,
    onBackground = ZapoteOscuro,
    surface = Color.White,
    onSurface = ZapoteOscuro,
    surfaceVariant = ZapoteClaro,
    onSurfaceVariant = ZapoteOscuro
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = EsquemaZapote) {
                // El contenedor Surface es fundamental para establecer el color de fondo base
                // de la aplicación utilizando la paleta de colores del MaterialTheme actual.
                Surface(modifier = Modifier.fillMaxSize()) {
                    PerfilScreen()
                }
            }
        }
    }
}

@Composable
fun PerfilScreen() {
    // rememberSaveable mantiene el estado incluso a través de cambios de configuración
    // como la rotación de la pantalla del dispositivo.
    var publicaciones by rememberSaveable { mutableStateOf(0) }
    var nombreUsuario by rememberSaveable { mutableStateOf("Sebastian Llano") }
    var modoEdicion by rememberSaveable { mutableStateOf(false) }
    var textoBorrador by rememberSaveable { mutableStateOf("") }

    // mutableStateListOf se utiliza para mantener un estado observable de una lista.
    // Aunque no usa rememberSaveable, se retiene en memoria mientras el composable está en la jerarquía.
    val listaLogros = remember { mutableStateListOf("Primer commit exitoso") }

    Column(
        // safeDrawingPadding evita la superposición del contenido con elementos del sistema 
        // como la barra de estado o la barra de navegación.
        // verticalScroll permite desplazar el contenido si este excede el tamaño de la pantalla.
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarPerfil(iniciales = generarIniciales(nombreUsuario))

        Spacer(modifier = Modifier.height(16.dp))

        // Si el usuario borra su nombre por completo, proporcionamos un valor de respaldo
        Text(
            text = nombreUsuario.ifBlank { "Usuario Desconocido" },
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Ingeniería en Informática · Código 55444",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            // El relleno (padding) se aplica internamente en la tarjeta para asegurar
            // que los bordes redondeados y la sombra se rendericen correctamente.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Publicaciones Realizadas", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = publicaciones.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Botón para reducir publicaciones. Se desactiva si llega a cero para evitar valores negativos.
                    Button(
                        onClick = { if (publicaciones > 0) publicaciones-- },
                        enabled = publicaciones > 0
                    ) { Text("Reducir") }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Botón para aumentar publicaciones con un límite arbitrario de seguridad
                    Button(
                        onClick = { if (publicaciones < 100) publicaciones++ },
                        enabled = publicaciones < 100
                    ) { Text("Aumentar") }
                }
                
                // Opción adicional para reiniciar el contador si supera cierto valor
                AnimatedVisibility(visible = publicaciones >= 10) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { publicaciones = 0 },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Reiniciar Contador")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        SeccionLogros(logros = listaLogros)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            textoBorrador = nombreUsuario
            modoEdicion = !modoEdicion
        }) {
            Text(if (modoEdicion) "Cancelar Edición" else "Modificar Perfil")
        }

        // AnimatedVisibility provee una transición de entrada y salida agradable
        // al mostrar y ocultar el formulario de edición.
        AnimatedVisibility(visible = modoEdicion) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                OutlinedTextField(
                    value = textoBorrador,
                    onValueChange = { textoBorrador = it },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        nombreUsuario = textoBorrador.trim().ifBlank { "Usuario Desconocido" }
                        modoEdicion = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar Cambios") }
            }
        }
    }
}

@Composable
fun AvatarPerfil(iniciales: String) {
    // El orden de los modificadores es crucial en Compose.
    // Primero definimos el tamaño, aplicamos el recorte circular y finalmente el color de fondo.
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun SeccionLogros(logros: MutableList<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Logros Adquiridos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        if (logros.isEmpty()) {
            Text(
                text = "No hay logros registrados.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            logros.forEachIndexed { index, logro ->
                Text(
                    text = "${index + 1}. $logro",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedButton(onClick = {
                val nuevoLogro = "Logro Desbloqueado Nivel ${logros.size + 1}"
                logros.add(nuevoLogro)
                // Registro de la acción en el Logcat para propósitos de depuración
                Log.d("PerfilApp", "Logro agregado: $nuevoLogro | Total: ${logros.size}")
            }) {
                Text("Añadir Logro")
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Botón para eliminar el último logro si la lista no está vacía
            OutlinedButton(
                onClick = { if (logros.isNotEmpty()) logros.removeLast() },
                enabled = logros.isNotEmpty(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Quitar")
            }
        }
    }
}

/**
 * Genera las iniciales del usuario basándose en su nombre completo.
 * Toma la primera letra de las dos primeras palabras.
 * Si el resultado está vacío, retorna un símbolo de interrogación por defecto.
 *
 * @param nombreCompleto El nombre del cual se extraerán las iniciales.
 * @return Un String con las iniciales en mayúscula.
 */
private fun generarIniciales(nombreCompleto: String): String =
    nombreCompleto.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "?" }

@Preview(showBackground = true, name = "Vista Previa Perfil", showSystemUi = true)
@Composable
fun VistaPreviaPerfilScreen() {
    MaterialTheme(colorScheme = EsquemaZapote) { PerfilScreen() }
}