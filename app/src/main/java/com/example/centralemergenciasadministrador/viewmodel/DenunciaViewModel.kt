package com.example.centralemergenciasadministrador.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralemergenciasadministrador.data.local.entities.DenunciaEntity
import com.example.centralemergenciasadministrador.data.repository.DenunciaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DenunciaViewModel(
    private val repository: DenunciaRepository
) : ViewModel() {

    val denuncias: StateFlow<List<DenunciaEntity>> =
        repository.obtenerDenuncias()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    val totalRecibidas: StateFlow<Int> =
        repository.contarPorEstado("RECIBIDO")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    val totalEnAtencion: StateFlow<Int> =
        repository.contarPorEstado("EN ATENCION")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    val totalCerradas: StateFlow<Int> =
        repository.contarPorEstado("CERRADO")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )
    fun actualizarDesdeServidor() {
        viewModelScope.launch {
            try {
                repository.actualizarDesdeServidor()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cambiarEstado(id: Int, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                repository.cambiarEstado(id, nuevoEstado)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun eliminarDenuncia(
        id: Int,
        onEliminado: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.eliminarDenuncia(id)
                onEliminado()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}