package com.example.centralemergenciasadministrador.data.repository

import com.example.centralemergenciasadministrador.data.local.dao.ComisariaDao
import com.example.centralemergenciasadministrador.data.local.entities.ComisariaEntity
import com.example.centralemergenciasadministrador.data.mapper.toEntity
import com.example.centralemergenciasadministrador.data.remote.api.ApiService
import kotlinx.coroutines.flow.Flow

class ComisariaRepository(
    private val comisariaDao: ComisariaDao,
    private val apiService: ApiService
) {

    fun obtenerComisarias(): Flow<List<ComisariaEntity>> {
        return comisariaDao.obtenerTodas()
    }

    suspend fun actualizarDesdeServidor() {
        val comisariasRemotas = apiService.obtenerComisarias()

        val comisariasLocales = comisariasRemotas.map { comisaria ->
            comisaria.toEntity()
        }

        comisariaDao.insertarTodas(comisariasLocales)
    }
}