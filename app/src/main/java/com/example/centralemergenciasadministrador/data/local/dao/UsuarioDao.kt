package com.example.centralemergenciasadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.centralemergenciasadministrador.data.local.entities.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios LIMIT 1")
    suspend fun obtenerUsuarioGuardado(): UsuarioEntity?

    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodos()
}