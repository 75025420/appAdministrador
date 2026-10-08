package com.example.centralemergenciasadministrador.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.centralemergenciasadministrador.data.local.AppDatabase
import com.example.centralemergenciasadministrador.data.local.SessionManager
import com.example.centralemergenciasadministrador.data.remote.RetrofitClient
import com.example.centralemergenciasadministrador.data.repository.AuthRepository
import com.example.centralemergenciasadministrador.data.repository.ComisariaRepository
import com.example.centralemergenciasadministrador.data.repository.DenunciaRepository
import com.example.centralemergenciasadministrador.data.repository.SyncRepository

class AppContainer(context: Context) {

    // Migración de Room versión 1 a 2
    private val migration1To2 = object : Migration(1, 2) {

        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `comisarias` (
                    `id` INTEGER NOT NULL,
                    `nombre` TEXT NOT NULL,
                    `region` TEXT NOT NULL,
                    `distrito` TEXT NOT NULL,
                    `tipo` TEXT NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
        }
    }

    // Migración de Room versión 2 a 3
    private val migration2To3 = object : Migration(2, 3) {

        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                "ALTER TABLE comisarias ADD COLUMN direccion TEXT NOT NULL DEFAULT ''"
            )

            db.execSQL(
                "ALTER TABLE comisarias ADD COLUMN telefono TEXT NOT NULL DEFAULT ''"
            )

            db.execSQL(
                "ALTER TABLE comisarias ADD COLUMN distancia REAL NOT NULL DEFAULT 0"
            )
        }
    }

    // Migración de Room versión 3 a 4
    private val migration3To4 = object : Migration(3, 4) {

        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `operaciones_pendientes` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `uuid` TEXT NOT NULL,
                    `tipoOperacion` TEXT NOT NULL,
                    `entidad` TEXT NOT NULL,
                    `entidadId` INTEGER NOT NULL,
                    `datos` TEXT NOT NULL,
                    `estado` TEXT NOT NULL,
                    `intentos` INTEGER NOT NULL,
                    `fechaCreacion` INTEGER NOT NULL,
                    `mensajeError` TEXT
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `sync_metadata` (
                    `id` INTEGER NOT NULL,
                    `ultimaSincronizacionExitosa` INTEGER,
                    `ultimaSincronizacionIntento` INTEGER,
                    `estado` TEXT NOT NULL,
                    `mensajeError` TEXT,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
        }
    }

    // Base de datos local Room
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "central_emergencias_admin.db"
    )
        .addMigrations(
            migration1To2,
            migration2To3,
            migration3To4
        )
        .build()

    // Administrador de sesión
    private val sessionManager =
        SessionManager(context.applicationContext)

    // Restaurar token guardado al iniciar la aplicación
    init {
        RetrofitClient.actualizarToken(
            sessionManager.obtenerToken()
        )
    }

    // Servicio Retrofit
    private val apiService = RetrofitClient.apiService

    // Repositorio de autenticación
    val authRepository = AuthRepository(
        apiService = apiService,
        usuarioDao = database.usuarioDao(),
        sessionManager = sessionManager
    )

    // Repositorio de denuncias
    val denunciaRepository = DenunciaRepository(
        database = database,
        denunciaDao = database.denunciaDao(),
        operacionPendienteDao = database.operacionPendienteDao(),
        apiService = apiService
    )

    // Repositorio de comisarías
    val comisariaRepository = ComisariaRepository(
        comisariaDao = database.comisariaDao(),
        apiService = apiService
    )

    // Repositorio de sincronización
    val syncRepository = SyncRepository(
        operacionPendienteDao = database.operacionPendienteDao(),
        syncMetadataDao = database.syncMetadataDao(),
        denunciaDao = database.denunciaDao(),
        apiService = apiService
    )
}