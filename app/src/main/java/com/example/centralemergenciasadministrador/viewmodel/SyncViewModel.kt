package com.example.centralemergenciasadministrador.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centralemergenciasadministrador.data.local.entities.OperacionPendienteEntity
import com.example.centralemergenciasadministrador.data.local.entities.SyncMetadataEntity
import com.example.centralemergenciasadministrador.data.repository.SyncRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SyncViewModel(
    private val repository: SyncRepository
) : ViewModel() {

    val operaciones: StateFlow<List<OperacionPendienteEntity>> =
        repository.obtenerOperaciones()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val totalPendientes: StateFlow<Int> =
        repository.contarPendientes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    val totalErrores: StateFlow<Int> =
        repository.contarErrores()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    val metadata: StateFlow<SyncMetadataEntity?> =
        repository.obtenerMetadata()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
}