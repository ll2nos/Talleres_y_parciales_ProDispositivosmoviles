package com.example.consumir_api.viewmodel

import com.example.consumir_api.model.Personaje

sealed interface UiState {
    data object Loading : UiState
    data class Success(val items: List<Personaje>) : UiState
    data class Error(val message: String) : UiState
}
