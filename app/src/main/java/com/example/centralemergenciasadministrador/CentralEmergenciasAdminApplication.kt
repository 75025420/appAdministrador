package com.example.centralemergenciasadministrador

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.centralemergenciasadministrador.di.AppContainer
import com.example.centralemergenciasadministrador.worker.SyncWorker
import java.util.concurrent.TimeUnit

class CentralEmergenciasAdminApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)

        programarSincronizacion()
    }

    private fun programarSincronizacion() {

        // El Worker solamente se ejecutará cuando exista conexión a Internet
        val restricciones = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // WorkManager ejecutará periódicamente la sincronización
        val solicitudSincronizacion =
            PeriodicWorkRequestBuilder<SyncWorker>(
                15,
                TimeUnit.MINUTES
            )
                .setConstraints(restricciones)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "sincronizacion_denuncias",
                ExistingPeriodicWorkPolicy.KEEP,
                solicitudSincronizacion
            )
    }
}