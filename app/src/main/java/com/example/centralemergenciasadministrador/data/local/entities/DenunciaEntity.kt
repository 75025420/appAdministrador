package com.example.centralemergenciasadministrador.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "denuncias")
data class DenunciaEntity(
    @PrimaryKey
    val id: Int,
    val idRemoto: Int? = null,
    val uuid: String,
    val codigo: String,
    val tipoIncidente: String,
    val ubicacion: String,
    val descripcion: String,
    val estado: String,
    val version: Int = 1,
    val estadoSincronizacion: String = "SINCRONIZADO"
)