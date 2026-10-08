package com.example.centralemergenciasadministrador.data.mapper

import com.example.centralemergenciasadministrador.data.local.entities.DenunciaEntity
import com.example.centralemergenciasadministrador.data.remote.dto.DenunciaDto

fun DenunciaDto.toEntity(): DenunciaEntity {
    return DenunciaEntity(
        id = id,
        idRemoto = id,
        uuid = uuid,
        codigo = codigo,
        tipoIncidente = tipoIncidente,
        ubicacion = ubicacion,
        descripcion = descripcion,
        estado = estado,
        version = version,
        estadoSincronizacion = "SINCRONIZADO"
    )
}