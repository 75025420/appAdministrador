package com.example.centralemergenciasadministrador.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.centralemergenciasadministrador.CentralEmergenciasAdminApplication

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val app =
            applicationContext as CentralEmergenciasAdminApplication

        val syncRepository =
            app.container.syncRepository

        return try {

            // Registrar que comenzó la sincronización
            syncRepository.registrarInicioSincronizacion()

            val operacionesPendientes =
                syncRepository.obtenerPendientes()

            var huboErrores = false
            var ultimoError = ""

            for (operacion in operacionesPendientes) {

                try {
                    syncRepository.marcarComoEnviando(operacion)

                    syncRepository.procesarOperacion(operacion)

                    syncRepository.marcarComoSincronizado(operacion)

                } catch (e: Exception) {

                    huboErrores = true
                    ultimoError =
                        e.message ?: "Error al sincronizar"

                    syncRepository.marcarComoError(
                        operacion,
                        ultimoError
                    )
                }
            }

            if (huboErrores) {

                syncRepository.registrarErrorSincronizacion(
                    ultimoError
                )

            } else {

                syncRepository.registrarSincronizacionExitosa()
            }

            Result.success()

        } catch (e: Exception) {

            syncRepository.registrarErrorSincronizacion(
                e.message ?: "Error general de sincronización"
            )

            Result.retry()
        }
    }
}