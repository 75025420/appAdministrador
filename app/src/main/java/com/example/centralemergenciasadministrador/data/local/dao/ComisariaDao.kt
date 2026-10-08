package com.example.centralemergenciasadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.centralemergenciasadministrador.data.local.entities.ComisariaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComisariaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(comisarias: List<ComisariaEntity>)

    @Query("SELECT * FROM comisarias ORDER BY nombre ASC")
    fun obtenerTodas(): Flow<List<ComisariaEntity>>

    @Query("DELETE FROM comisarias")
    suspend fun eliminarTodas()
}