package com.example.consumir_api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumir_api.model.Personaje
import com.example.consumir_api.network.RetrofitInstance
import com.example.consumir_api.repository.PersonajesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PersonajesViewModel(
    private val repository: PersonajesRepository = PersonajesRepository(RetrofitInstance.api)
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    private val _detalleState = MutableStateFlow<DetalleUiState>(DetalleUiState.Loading)
    val detalleState: StateFlow<DetalleUiState> = _detalleState.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.obtenerPersonajes().fold(
                onSuccess = { _uiState.value = UiState.Success(it.results) },
                onFailure = { 
                    val msg = when (it) {
                        is IOException -> "Error de conexión. Verifica tu internet."
                        is HttpException -> "Error del servidor (${it.code()})."
                        else -> "Error inesperado."
                    }
                    _uiState.value = UiState.Error(msg)
                }
            )
        }
    }
    
    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _detalleState.value = DetalleUiState.Loading
            repository.obtenerPersonaje(id).fold(
                onSuccess = { _detalleState.value = DetalleUiState.Success(it) },
                onFailure = {
                    val msg = when (it) {
                        is IOException -> "Error de conexión. Verifica tu internet."
                        is HttpException -> "Error del servidor (${it.code()})."
                        else -> "Error inesperado."
                    }
                    _detalleState.value = DetalleUiState.Error(msg)
                }
            )
        }
    }
}

sealed interface DetalleUiState {
    data object Loading : DetalleUiState
    data class Success(val personaje: Personaje) : DetalleUiState
    data class Error(val message: String) : DetalleUiState
}
