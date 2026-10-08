package com.example.centralemergenciasadministrador.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralemergenciasadministrador.data.local.entities.ComisariaEntity
import com.example.centralemergenciasadministrador.data.repository.ComisariaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ComisariaViewModel(
    private val repository: ComisariaRepository
) : ViewModel() {

    val comisarias: StateFlow<List<ComisariaEntity>> =
        repository.obtenerComisarias()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
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
}