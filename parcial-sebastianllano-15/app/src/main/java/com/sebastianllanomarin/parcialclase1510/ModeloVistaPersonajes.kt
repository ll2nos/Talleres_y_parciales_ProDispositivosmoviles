package com.sebastianllanomarin.parcialclase1510

// Parcial clase 15_10 - Sebastian Llano Marin - codigo 55444
// Estado y logica de negocio de la app. Esta clase no conoce nada de Compose:
// solo expone flujos de datos que las pantallas observan.

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Foto completa del estado de la pantalla. Se guarda todo junto en un único
 * objeto inmutable para que cada cambio sea atómico (no hay estados a medias).
 */
data class EstadoPantalla(
    val heroes: List<Elemento> = emptyList(),
    val villanos: List<Elemento> = emptyList(),
    val cargando: Boolean = true,           // true al inicio para mostrar el indicador de carga
    val mostrandoVillanos: Boolean = false  // false = héroes, true = villanos
)

class ModeloVistaPersonajes : ViewModel() {

    // Fuente única de verdad. Es privada: solo esta clase puede modificarla.
    private val _estado = MutableStateFlow(EstadoPantalla())

    // Flujos derivados de solo lectura que consumen las pantallas.
    // WhileSubscribed(5000) mantiene el valor vivo 5 s al rotar la pantalla.
    val cargando: StateFlow<Boolean> = _estado
        .map { it.cargando }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val verVillanos: StateFlow<Boolean> = _estado
        .map { it.mostrandoVillanos }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Lista visible: se elige la categoría activa y se ordena alfabéticamente.
    val elementosFiltrados: StateFlow<List<Elemento>> = _estado
        .map { e ->
            val base = if (e.mostrandoVillanos) e.villanos else e.heroes
            base.sortedBy { it.nombre.lowercase() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // El ViewModel sobrevive a la rotación, por eso la carga solo ocurre una vez.
    init {
        cargarElementos()
    }

    /** Simula una carga remota de un segundo y luego publica ambas listas. */
    fun cargarElementos() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true) }
            delay(1000) // espera artificial para ver el indicador de progreso
            val heroes = obtenerSuperheroes()
            val villanos = obtenerVillanos()
            _estado.update { it.copy(heroes = heroes, villanos = villanos, cargando = false) }
        }
    }

    /** Cambia entre la lista de héroes (false) y la de villanos (true). */
    fun mostrarVillanos(villanos: Boolean) {
        _estado.update { it.copy(mostrandoVillanos = villanos) }
    }

    /** Busca un personaje por id en ambas listas; lo usa la pantalla de detalle. */
    fun elementoPorId(id: Int): Flow<Elemento?> =
        _estado.map { e -> (e.heroes + e.villanos).firstOrNull { it.id == id } }
}
