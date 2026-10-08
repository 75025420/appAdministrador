package com.example.centralemergenciasadministrador.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.centralemergenciasadministrador.viewmodel.DenunciaViewModel

@Composable
fun ListScreen(
    denunciaViewModel: DenunciaViewModel,
    onVerDetalle: (Int) -> Unit
) {
    val denuncias by denunciaViewModel.denuncias.collectAsState()

    val totalRecibidas by denunciaViewModel.totalRecibidas.collectAsState()
    val totalEnAtencion by denunciaViewModel.totalEnAtencion.collectAsState()
    val totalCerradas by denunciaViewModel.totalCerradas.collectAsState()

    LaunchedEffect(Unit) {
        denunciaViewModel.actualizarDesdeServidor()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Denuncias registradas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("RECIBIDO")
                    Text(
                        text = totalRecibidas.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("EN ATENCIÓN")
                    Text(
                        text = totalEnAtencion.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("CERRADO")
                    Text(
                        text = totalCerradas.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                denunciaViewModel.actualizarDesdeServidor()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar desde servidor")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (denuncias.isEmpty()) {

            Text("No hay denuncias registradas.")

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = denuncias,
                    key = { it.id }
                ) { denuncia ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onVerDetalle(denuncia.id)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = denuncia.codigo,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Incidente: ${denuncia.tipoIncidente}"
                            )

                            Text(
                                text = "Ubicación: ${denuncia.ubicacion}"
                            )

                            Text(
                                text = "Estado: ${denuncia.estado}"
                            )
                        }
                    }
                }
            }
        }
    }
}