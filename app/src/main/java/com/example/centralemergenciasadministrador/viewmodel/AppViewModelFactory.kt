package com.example.centralemergenciasadministrador.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.centralemergenciasadministrador.data.repository.AuthRepository
import com.example.centralemergenciasadministrador.data.repository.ComisariaRepository
import com.example.centralemergenciasadministrador.data.repository.DenunciaRepository
import com.example.centralemergenciasadministrador.data.repository.SyncRepository

class AppViewModelFactory(
    private val authRepository: AuthRepository,
    private val denunciaRepository: DenunciaRepository,
    private val comisariaRepository: ComisariaRepository,
    private val syncRepository: SyncRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(authRepository) as T

            modelClass.isAssignableFrom(DenunciaViewModel::class.java) ->
                DenunciaViewModel(denunciaRepository) as T

            modelClass.isAssignableFrom(ComisariaViewModel::class.java) ->
                ComisariaViewModel(comisariaRepository) as T

            modelClass.isAssignableFrom(SyncViewModel::class.java) ->
                SyncViewModel(syncRepository) as T

            else ->
                throw IllegalArgumentException(
                    "ViewModel desconocido: ${modelClass.name}"
                )
        }
    }
}