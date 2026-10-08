package com.example.centralemergenciasadministrador.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.centralemergenciasadministrador.data.local.dao.ComisariaDao
import com.example.centralemergenciasadministrador.data.local.dao.DenunciaDao
import com.example.centralemergenciasadministrador.data.local.dao.OperacionPendienteDao
import com.example.centralemergenciasadministrador.data.local.dao.UsuarioDao
import com.example.centralemergenciasadministrador.data.local.entities.ComisariaEntity
import com.example.centralemergenciasadministrador.data.local.entities.DenunciaEntity
import com.example.centralemergenciasadministrador.data.local.entities.OperacionPendienteEntity
import com.example.centralemergenciasadministrador.data.local.entities.SyncMetadataEntity
import com.example.centralemergenciasadministrador.data.local.entities.UsuarioEntity
import com.example.centralemergenciasadministrador.data.local.dao.SyncMetadataDao

@Database(
    entities = [
        UsuarioEntity::class,
        DenunciaEntity::class,
        ComisariaEntity::class,
        OperacionPendienteEntity::class,
        SyncMetadataEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun denunciaDao(): DenunciaDao

    abstract fun comisariaDao(): ComisariaDao

    abstract fun operacionPendienteDao(): OperacionPendienteDao

    abstract fun syncMetadataDao(): SyncMetadataDao
}