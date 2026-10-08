package com.example.centralemergenciasadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.centralemergenciasadministrador.data.local.entities.DenunciaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DenunciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(denuncias: List<DenunciaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(denuncia: DenunciaEntity)

    @Update
    suspend fun actualizar(denuncia: DenunciaEntity)

    @Delete
    suspend fun eliminar(denuncia: DenunciaEntity)

    @Query("SELECT * FROM denuncias ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<DenunciaEntity>>

    @Query("SELECT * FROM denuncias WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): DenunciaEntity?

    @Query("SELECT COUNT(*) FROM denuncias WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query("DELETE FROM denuncias")
    suspend fun eliminarTodas()
}