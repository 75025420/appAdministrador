package com.example.centralemergenciasadministrador.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralemergenciasadministrador.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _estadoLogin =
        MutableStateFlow<EstadoLogin>(EstadoLogin.Inicial)

    val estadoLogin: StateFlow<EstadoLogin> =
        _estadoLogin.asStateFlow()

    private val _estadoSesion =
        MutableStateFlow<EstadoSesion>(EstadoSesion.Verificando)

    val estadoSesion: StateFlow<EstadoSesion> =
        _estadoSesion.asStateFlow()

    fun verificarSesion() {
        viewModelScope.launch {
            _estadoSesion.value = EstadoSesion.Verificando

            _estadoSesion.value = try {
                if (authRepository.haySesionGuardada()) {
                    EstadoSesion.Autenticado
                } else {
                    EstadoSesion.SinSesion
                }
            } catch (e: Exception) {
                EstadoSesion.SinSesion
            }
        }
    }

    fun login(correo: String, password: String) {

        if (correo.isBlank() || password.isBlank()) {
            _estadoLogin.value =
                EstadoLogin.Error("Ingrese correo y contraseña")
            return
        }

        viewModelScope.launch {
            _estadoLogin.value = EstadoLogin.Cargando

            val resultado = authRepository.login(correo, password)

            _estadoLogin.value = resultado.fold(
                onSuccess = {
                    _estadoSesion.value = EstadoSesion.Autenticado
                    EstadoLogin.Exito
                },
                onFailure = {
                    EstadoLogin.Error(
                        it.message ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            authRepository.cerrarSesion()

            _estadoLogin.value = EstadoLogin.Inicial
            _estadoSesion.value = EstadoSesion.SinSesion
        }
    }
}

sealed class EstadoLogin {
    data object Inicial : EstadoLogin()
    data object Cargando : EstadoLogin()
    data object Exito : EstadoLogin()
    data class Error(val mensaje: String) : EstadoLogin()
}

sealed class EstadoSesion {
    data object Verificando : EstadoSesion()
    data object Autenticado : EstadoSesion()
    data object SinSesion : EstadoSesion()
}