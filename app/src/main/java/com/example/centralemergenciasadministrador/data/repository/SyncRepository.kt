package com.example.centralemergenciasadministrador.data.repository

import com.example.centralemergenciasadministrador.data.local.dao.DenunciaDao
import com.example.centralemergenciasadministrador.data.local.dao.OperacionPendienteDao
import com.example.centralemergenciasadministrador.data.local.dao.SyncMetadataDao
import com.example.centralemergenciasadministrador.data.local.entities.OperacionPendienteEntity
import com.example.centralemergenciasadministrador.data.local.entities.SyncMetadataEntity
import com.example.centralemergenciasadministrador.data.remote.api.ApiService
import com.example.centralemergenciasadministrador.data.remote.api.EstadoRequest
import kotlinx.coroutines.flow.Flow

class SyncRepository(
    private val operacionPendienteDao: OperacionPendienteDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val denunciaDao: DenunciaDao,
    private val apiService: ApiService
) {

    fun obtenerOperaciones(): Flow<List<OperacionPendienteEntity>> {
        return operacionPendienteDao.obtenerTodas()
    }

    fun contarPendientes(): Flow<Int> {
        return operacionPendienteDao.contarPendientes()
    }

    fun contarErrores(): Flow<Int> {
        return operacionPendienteDao.contarErrores()
    }

    fun obtenerMetadata(): Flow<SyncMetadataEntity?> {
        return syncMetadataDao.obtenerMetadata()
    }

    suspend fun registrarOperacion(
        uuid: String,
        tipoOperacion: String,
        entidad: String,
        entidadId: Int,
        datos: String
    ) {
        val operacion = OperacionPendienteEntity(
            uuid = uuid,
            tipoOperacion = tipoOperacion,
            entidad = entidad,
            entidadId = entidadId,
            datos = datos,
            estado = "PENDIENTE"
        )

        operacionPendienteDao.insertar(operacion)
    }

    suspend fun obtenerPendientes(): List<OperacionPendienteEntity> {
        return operacionPendienteDao.obtenerPendientes()
    }

    suspend fun marcarComoEnviando(
        operacion: OperacionPendienteEntity
    ) {
        operacionPendienteDao.actualizar(
            operacion.copy(
                estado = "ENVIANDO",
                mensajeError = null
            )
        )
    }

    suspend fun marcarComoError(
        operacion: OperacionPendienteEntity,
        mensaje: String
    ) {
        operacionPendienteDao.actualizar(
            operacion.copy(
                estado = "ERROR",
                intentos = operacion.intentos + 1,
                mensajeError = mensaje
            )
        )
    }

    suspend fun marcarComoSincronizado(
        operacion: OperacionPendienteEntity
    ) {
        // Marcar la operación como sincronizada
        operacionPendienteDao.actualizar(
            operacion.copy(
                estado = "SINCRONIZADO",
                mensajeError = null
            )
        )

        // Actualizar también el estado local de la denuncia
        if (operacion.tipoOperacion == "CAMBIAR_ESTADO") {

            val denuncia = denunciaDao.obtenerPorId(
                operacion.entidadId
            )

            if (
                denuncia != null &&
                denuncia.estado == operacion.datos
            ) {
                denunciaDao.actualizar(
                    denuncia.copy(
                        estadoSincronizacion = "SINCRONIZADO"
                    )
                )
            }
        }
    }

    suspend fun procesarOperacion(
        operacion: OperacionPendienteEntity
    ) {
        when (operacion.tipoOperacion) {

            "CAMBIAR_ESTADO" -> {
                apiService.cambiarEstado(
                    id = operacion.entidadId,
                    request = EstadoRequest(
                        estado = operacion.datos
                    )
                )
            }

            "ELIMINAR" -> {
                apiService.eliminarDenuncia(
                    id = operacion.entidadId
                )
            }

            else -> {
                throw IllegalArgumentException(
                    "Tipo de operación no reconocido: ${operacion.tipoOperacion}"
                )
            }
        }
    }

    suspend fun registrarInicioSincronizacion() {

        val metadataActual =
            syncMetadataDao.obtenerMetadataUnaVez()

        syncMetadataDao.guardar(
            SyncMetadataEntity(
                id = 1,
                ultimaSincronizacionExitosa =
                    metadataActual?.ultimaSincronizacionExitosa,
                ultimaSincronizacionIntento =
                    System.currentTimeMillis(),
                estado = "SINCRONIZANDO",
                mensajeError = null
            )
        )
    }

    suspend fun registrarSincronizacionExitosa() {

        val ahora = System.currentTimeMillis()

        syncMetadataDao.guardar(
            SyncMetadataEntity(
                id = 1,
                ultimaSincronizacionExitosa = ahora,
                ultimaSincronizacionIntento = ahora,
                estado = "SINCRONIZADO",
                mensajeError = null
            )
        )
    }

    suspend fun registrarErrorSincronizacion(
        mensaje: String
    ) {

        val metadataActual =
            syncMetadataDao.obtenerMetadataUnaVez()

        syncMetadataDao.guardar(
            SyncMetadataEntity(
                id = 1,
                ultimaSincronizacionExitosa =
                    metadataActual?.ultimaSincronizacionExitosa,
                ultimaSincronizacionIntento =
                    System.currentTimeMillis(),
                estado = "ERROR",
                mensajeError = mensaje
            )
        )
    }

    suspend fun eliminarOperacion(id: Int) {
        operacionPendienteDao.eliminarPorId(id)
    }
}