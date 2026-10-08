package com.example.centralemergenciasadministrador.data.remote.dto

data class DenunciaDto(
    val id: Int,
    val uuid: String,
    val codigo: String,
    val tipoIncidente: String,
    val ubicacion: String,
    val descripcion: String,
    val estado: String,
    val version: Int
)