package com.example.centralemergenciasadministrador.data.remote.api

import com.example.centralemergenciasadministrador.data.remote.dto.DenunciaDto
import com.example.centralemergenciasadministrador.data.remote.dto.LoginRequest
import com.example.centralemergenciasadministrador.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import com.example.centralemergenciasadministrador.data.remote.dto.ComisariaDto

data class EstadoRequest(
    val estado: String
)

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("denuncias")
    suspend fun obtenerDenuncias(): List<DenunciaDto>

    @GET("comisarias")
    suspend fun obtenerComisarias(): List<ComisariaDto>

    @PUT("denuncias/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Int,
        @Body request: EstadoRequest
    ): DenunciaDto

    @DELETE("denuncias/{id}")
    suspend fun eliminarDenuncia(
        @Path("id") id: Int
    )
}