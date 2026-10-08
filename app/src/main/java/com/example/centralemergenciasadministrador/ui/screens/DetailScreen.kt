package com.example.centralemergenciasadministrador.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.centralemergenciasadministrador.viewmodel.DenunciaViewModel

@Composable
fun DetailScreen(
    denunciaId: Int,
    denunciaViewModel: DenunciaViewModel,
    onVolver: () -> Unit
) {
    val denuncias by denunciaViewModel.denuncias.collectAsState()
    val denuncia = denuncias.find { it.id == denunciaId }

    var mostrarConfirmacionEliminar by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Detalle de denuncia",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (denuncia == null) {

            Text("No se encontró la denuncia.")

        } else {

            Text(
                text = "Código: ${denuncia.codigo}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Tipo de incidente: ${denuncia.tipoIncidente}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Ubicación: ${denuncia.ubicacion}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Descripción: ${denuncia.descripcion}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Estado: ${denuncia.estado}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Versión: ${denuncia.version}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    denunciaViewModel.cambiarEstado(
                        denuncia.id,
                        "RECIBIDO"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Marcar como RECIBIDO")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    denunciaViewModel.cambiarEstado(
                        denuncia.id,
                        "EN ATENCION"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Marcar como EN ATENCIÓN")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    denunciaViewModel.cambiarEstado(
                        denuncia.id,
                        "CERRADO"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Marcar como CERRADO")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    mostrarConfirmacionEliminar = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar denuncia")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }

    if (mostrarConfirmacionEliminar && denuncia != null) {

        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacionEliminar = false
            },
            title = {
                Text("Eliminar denuncia")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar la denuncia ${denuncia.codigo}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionEliminar = false

                        denunciaViewModel.eliminarDenuncia(
                            denuncia.id
                        ) {
                            onVolver()
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionEliminar = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}