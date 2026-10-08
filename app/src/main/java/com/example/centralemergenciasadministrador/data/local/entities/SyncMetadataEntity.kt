package com.example.centralemergenciasadministrador.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity(
    @PrimaryKey
    val id: Int = 1,
    val ultimaSincronizacionExitosa: Long? = null,
    val ultimaSincronizacionIntento: Long? = null,
    val estado: String = "PENDIENTE",
    val mensajeError: String? = null
)