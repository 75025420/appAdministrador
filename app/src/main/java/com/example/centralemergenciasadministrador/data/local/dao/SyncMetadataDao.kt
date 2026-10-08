package com.example.centralemergenciasadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.centralemergenciasadministrador.data.local.entities.SyncMetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncMetadataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(metadata: SyncMetadataEntity)

    @Query("SELECT * FROM sync_metadata WHERE id = 1 LIMIT 1")
    fun obtenerMetadata(): Flow<SyncMetadataEntity?>

    @Query("SELECT * FROM sync_metadata WHERE id = 1 LIMIT 1")
    suspend fun obtenerMetadataUnaVez(): SyncMetadataEntity?
}