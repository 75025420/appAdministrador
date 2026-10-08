package com.example.centralemergenciasadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.centralemergenciasadministrador.data.local.entities.OperacionPendienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperacionPendienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(operacion: OperacionPendienteEntity)

    @Update
    suspend fun actualizar(operacion: OperacionPendienteEntity)

    @Query("SELECT * FROM operaciones_pendientes ORDER BY fechaCreacion ASC")
    fun obtenerTodas(): Flow<List<OperacionPendienteEntity>>

    @Query("""
    SELECT * FROM operaciones_pendientes
    WHERE estado = 'PENDIENTE' OR estado = 'ERROR'
    ORDER BY fechaCreacion ASC
""")
    suspend fun obtenerPendientes(): List<OperacionPendienteEntity>

    @Query("SELECT COUNT(*) FROM operaciones_pendientes WHERE estado = 'PENDIENTE'")
    fun contarPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM operaciones_pendientes WHERE estado = 'ERROR'")
    fun contarErrores(): Flow<Int>

    @Query("DELETE FROM operaciones_pendientes WHERE id = :id")
    suspend fun eliminarPorId(id: Int)
}