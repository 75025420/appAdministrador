package com.example.centralemergenciasadministrador.data.remote.dto

data class ComisariaDto(
    val id: Int,
    val nombre: String,
    val region: String,
    val distrito: String,
    val tipo: String,
    val direccion: String,
    val telefono: String,
    val distancia: Double
)