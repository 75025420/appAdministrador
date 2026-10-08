package com.example.centralemergenciasadministrador.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comisarias")
data class ComisariaEntity(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val region: String,
    val distrito: String,
    val tipo: String,
    val direccion: String,
    val telefono: String,
    val distancia: Double
)