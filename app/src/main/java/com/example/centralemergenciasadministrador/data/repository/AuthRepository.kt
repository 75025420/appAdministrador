package com.example.centralemergenciasadministrador.data.repository

import com.example.centralemergenciasadministrador.data.local.SessionManager
import com.example.centralemergenciasadministrador.data.local.dao.UsuarioDao
import com.example.centralemergenciasadministrador.data.local.entities.UsuarioEntity
import com.example.centralemergenciasadministrador.data.remote.RetrofitClient
import com.example.centralemergenciasadministrador.data.remote.api.ApiService
import com.example.centralemergenciasadministrador.data.remote.dto.LoginRequest

class AuthRepository(
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDao,
    private val sessionManager: SessionManager
) {

    suspend fun login(
        correo: String,
        password: String
    ): Result<UsuarioEntity> {

        return try {

            val respuesta = apiService.login(
                LoginRequest(
                    correo = correo,
                    password = password
                )
            )

            // Esta aplicación solo permite administradores
            if (respuesta.usuario.rol.lowercase() != "administrador") {
                return Result.failure(
                    Exception(
                        "Este usuario no tiene permisos de administrador"
                    )
                )
            }

            // Guardar token de forma persistente
            sessionManager.guardarToken(respuesta.token)

            // Usar token en Retrofit
            RetrofitClient.actualizarToken(respuesta.token)

            val usuario = UsuarioEntity(
                id = respuesta.usuario.id,
                nombre = respuesta.usuario.nombre,
                correo = respuesta.usuario.correo,
                rol = respuesta.usuario.rol
            )

            usuarioDao.eliminarTodos()
            usuarioDao.insertar(usuario)

            Result.success(usuario)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerUsuarioGuardado(): UsuarioEntity? {
        return usuarioDao.obtenerUsuarioGuardado()
    }

    suspend fun haySesionGuardada(): Boolean {

        val usuario = usuarioDao.obtenerUsuarioGuardado()
        val token = sessionManager.obtenerToken()

        if (usuario != null && !token.isNullOrBlank()) {

            RetrofitClient.actualizarToken(token)

            return true
        }

        return false
    }

    suspend fun cerrarSesion() {

        // Eliminar token de Retrofit
        RetrofitClient.actualizarToken(null)

        // Eliminar token persistente
        sessionManager.eliminarToken()

        // Eliminar usuario local
        usuarioDao.eliminarTodos()
    }
}