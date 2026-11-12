package com.example.consumir_api.repository

import com.example.consumir_api.model.Personaje
import com.example.consumir_api.model.PersonajesResponse
import com.example.consumir_api.network.RickMortyApi
import kotlinx.coroutines.CancellationException

class PersonajesRepository(private val api: RickMortyApi) {
    suspend fun obtenerPersonajes(): Result<PersonajesResponse> =
        try {
            Result.success(api.obtenerPersonajes())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun obtenerPersonaje(id: Int): Result<Personaje> =
        try {
            Result.success(api.obtenerPersonaje(id))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
