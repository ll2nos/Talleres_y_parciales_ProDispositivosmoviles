package com.example.consumir_api.model

import com.google.gson.annotations.SerializedName

data class Personaje(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    @SerializedName("image") val imagenUrl: String
)
