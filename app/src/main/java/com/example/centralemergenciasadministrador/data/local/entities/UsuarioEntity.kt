package com.example.centralemergenciasadministrador.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: String
)