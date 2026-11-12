package com.example.consumir_api.network

import com.example.consumir_api.model.Personaje
import com.example.consumir_api.model.PersonajesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyApi {
    @GET("character")
    suspend fun obtenerPersonajes(): PersonajesResponse

    @GET("character/{id}")
    suspend fun obtenerPersonaje(@Path("id") id: Int): Personaje
}
