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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.centralemergenciasadministrador.ui.components.SyncStatusIndicator
import com.example.centralemergenciasadministrador.viewmodel.SyncViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SyncScreen(
    syncViewModel: SyncViewModel,
    onSincronizar: () -> Unit
) {

    val pendientes by syncViewModel.totalPendientes.collectAsState()
    val errores by syncViewModel.totalErrores.collectAsState()
    val metadata by syncViewModel.metadata.collectAsState()

    val ultimaSincronizacion =
        metadata?.ultimaSincronizacionExitosa?.let { fecha ->
            SimpleDateFormat(
                "dd/MM/yyyy hh:mm a",
                Locale.getDefault()
            ).format(Date(fecha))
        } ?: "Aún no se ha sincronizado"

    val estado =
        metadata?.estado ?: "SIN DATOS"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Sincronización",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Operaciones pendientes: $pendientes",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Errores de sincronización: $errores",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Estado: $estado",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        SyncStatusIndicator(
            estado = estado,
            pendientes = pendientes
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Última sincronización exitosa:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = ultimaSincronizacion
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSincronizar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sincronizar ahora")
        }
    }
}