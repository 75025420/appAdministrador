package com.example.centralemergenciasadministrador.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onVerDenuncias: () -> Unit,
    onVerComisarias: () -> Unit,
    onVerSincronizacion: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Panel de Administrador",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Central de Emergencias PNP",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onVerDenuncias,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar denuncias")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerComisarias,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver comisarías")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerSincronizacion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sincronización")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}