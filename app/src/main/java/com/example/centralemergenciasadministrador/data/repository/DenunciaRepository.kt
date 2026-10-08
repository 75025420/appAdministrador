package com.example.centralemergenciasadministrador.data.repository

import androidx.room.withTransaction
import com.example.centralemergenciasadministrador.data.local.AppDatabase
import com.example.centralemergenciasadministrador.data.local.dao.DenunciaDao
import com.example.centralemergenciasadministrador.data.local.dao.OperacionPendienteDao
import com.example.centralemergenciasadministrador.data.local.entities.DenunciaEntity
import com.example.centralemergenciasadministrador.data.local.entities.OperacionPendienteEntity
import com.example.centralemergenciasadministrador.data.mapper.toEntity
import com.example.centralemergenciasadministrador.data.remote.api.ApiService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class DenunciaRepository(
    private val database: AppDatabase,
    private val denunciaDao: DenunciaDao,
    private val operacionPendienteDao: OperacionPendienteDao,
    private val apiService: ApiService
) {

    fun obtenerDenuncias(): Flow<List<DenunciaEntity>> {
        return denunciaDao.obtenerTodas()
    }

    fun contarPorEstado(estado: String): Flow<Int> {
        return denunciaDao.contarPorEstado(estado)
    }

    suspend fun actualizarDesdeServidor() {
        val denunciasRemotas = apiService.obtenerDenuncias()

        for (denunciaRemota in denunciasRemotas) {

            val denunciaServidor = denunciaRemota.toEntity()
            val denunciaLocal =
                denunciaDao.obtenerPorId(denunciaServidor.id)

            if (
                denunciaLocal == null ||
                denunciaLocal.estadoSincronizacion != "PENDIENTE"
            ) {
                denunciaDao.insertar(denunciaServidor)
            }
        }
    }

    suspend fun obtenerPorId(id: Int): DenunciaEntity? {
        return denunciaDao.obtenerPorId(id)
    }

    suspend fun cambiarEstado(
        id: Int,
        nuevoEstado: String
    ) {
        val denuncia = denunciaDao.obtenerPorId(id)
            ?: throw Exception("Denuncia no encontrada")

        database.withTransaction {

            denunciaDao.actualizar(
                denuncia.copy(
                    estado = nuevoEstado,
                    estadoSincronizacion = "PENDIENTE"
                )
            )

            operacionPendienteDao.insertar(
                OperacionPendienteEntity(
                    uuid = UUID.randomUUID().toString(),
                    tipoOperacion = "CAMBIAR_ESTADO",
                    entidad = "DENUNCIA",
                    entidadId = id,
                    datos = nuevoEstado,
                    estado = "PENDIENTE"
                )
            )
        }
    }

    suspend fun eliminarDenuncia(id: Int) {
        val denuncia = denunciaDao.obtenerPorId(id)
            ?: throw Exception("Denuncia no encontrada")

        database.withTransaction {

            operacionPendienteDao.insertar(
                OperacionPendienteEntity(
                    uuid = UUID.randomUUID().toString(),
                    tipoOperacion = "ELIMINAR",
                    entidad = "DENUNCIA",
                    entidadId = id,
                    datos = "",
                    estado = "PENDIENTE"
                )
            )

            denunciaDao.eliminar(denuncia)
        }
    }
}