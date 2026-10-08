package com.example.centralemergenciasadministrador.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SyncStatusIndicator(
    estado: String,
    pendientes: Int
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = when (estado) {
                "SINCRONIZANDO" -> "⟳"
                "SINCRONIZADO" -> "✓"
                "ERROR" -> "!"
                else -> "•"
            },
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = when {
                estado == "SINCRONIZANDO" ->
                    "Sincronizando..."

                estado == "ERROR" ->
                    "Error de sincronización"

                pendientes > 0 ->
                    "$pendientes operación(es) pendiente(s)"

                estado == "SINCRONIZADO" ->
                    "Datos sincronizados"

                else ->
                    "Sin sincronizar"
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}