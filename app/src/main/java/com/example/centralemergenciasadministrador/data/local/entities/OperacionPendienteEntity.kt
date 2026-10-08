package com.example.centralemergenciasadministrador.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operaciones_pendientes")
data class OperacionPendienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uuid: String,
    val tipoOperacion: String,
    val entidad: String,
    val entidadId: Int,
    val datos: String,
    val estado: String = "PENDIENTE",
    val intentos: Int = 0,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val mensajeError: String? = null
)