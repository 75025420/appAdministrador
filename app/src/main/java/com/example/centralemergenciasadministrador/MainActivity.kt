package com.example.centralemergenciasadministrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.centralemergenciasadministrador.ui.components.ConnectivityBanner
import com.example.centralemergenciasadministrador.ui.navigation.AppNavigation
import com.example.centralemergenciasadministrador.ui.theme.CentralEmergenciasAdministradorTheme
import com.example.centralemergenciasadministrador.util.ConnectivityObserver

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectivityObserver = ConnectivityObserver(this)

        setContent {
            CentralEmergenciasAdministradorTheme {

                val conectado by connectivityObserver
                    .observarConexion()
                    .collectAsState(initial = true)

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    ConnectivityBanner(
                        conectado = conectado
                    )

                    AppNavigation(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}