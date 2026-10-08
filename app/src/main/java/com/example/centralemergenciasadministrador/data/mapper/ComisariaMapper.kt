package com.example.centralemergenciasadministrador.data.mapper

import com.example.centralemergenciasadministrador.data.local.entities.ComisariaEntity
import com.example.centralemergenciasadministrador.data.remote.dto.ComisariaDto

fun ComisariaDto.toEntity(): ComisariaEntity {
    return ComisariaEntity(
        id = id,
        nombre = nombre,
        region = region,
        distrito = distrito,
        tipo = tipo,
        direccion = direccion,
        telefono = telefono,
        distancia = distancia
    )
}