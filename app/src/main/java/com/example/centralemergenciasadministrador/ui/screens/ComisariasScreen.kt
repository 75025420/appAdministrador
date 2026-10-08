package com.example.centralemergenciasadministrador.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.centralemergenciasadministrador.viewmodel.ComisariaViewModel

@Composable
fun ComisariasScreen(
    comisariaViewModel: ComisariaViewModel
) {
    val comisarias by comisariaViewModel.comisarias.collectAsState()

    var regionSeleccionada by remember { mutableStateOf("Todas") }
    var distritoSeleccionado by remember { mutableStateOf("Todos") }
    var tipoSeleccionado by remember { mutableStateOf("Todos") }

    LaunchedEffect(Unit) {
        comisariaViewModel.actualizarDesdeServidor()
    }

    val regiones = listOf("Todas") +
            comisarias.map { it.region }.distinct().sorted()

    val distritos = listOf("Todos") +
            comisarias
                .filter {
                    regionSeleccionada == "Todas" ||
                            it.region == regionSeleccionada
                }
                .map { it.distrito }
                .distinct()
                .sorted()

    val tipos = listOf("Todos") +
            comisarias.map { it.tipo }.distinct().sorted()

    val comisariasFiltradas = comisarias.filter { comisaria ->

        val coincideRegion =
            regionSeleccionada == "Todas" ||
                    comisaria.region == regionSeleccionada

        val coincideDistrito =
            distritoSeleccionado == "Todos" ||
                    comisaria.distrito == distritoSeleccionado

        val coincideTipo =
            tipoSeleccionado == "Todos" ||
                    comisaria.tipo == tipoSeleccionado

        coincideRegion && coincideDistrito && coincideTipo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Lista de Comisarías",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                comisariaViewModel.actualizarDesdeServidor()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar desde servidor")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Filtrar por región",
            style = MaterialTheme.typography.labelLarge
        )

        FiltroDropdown(
            opciones = regiones,
            seleccion = regionSeleccionada,
            onSeleccion = {
                regionSeleccionada = it
                distritoSeleccionado = "Todos"
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Filtrar por distrito",
            style = MaterialTheme.typography.labelLarge
        )

        FiltroDropdown(
            opciones = distritos,
            seleccion = distritoSeleccionado,
            onSeleccion = {
                distritoSeleccionado = it
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Filtrar por tipo",
            style = MaterialTheme.typography.labelLarge
        )

        FiltroDropdown(
            opciones = tipos,
            seleccion = tipoSeleccionado,
            onSeleccion = {
                tipoSeleccionado = it
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                regionSeleccionada = "Todas"
                distritoSeleccionado = "Todos"
                tipoSeleccionado = "Todos"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar filtros")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (comisariasFiltradas.isEmpty()) {

            Text("No hay comisarías que coincidan con los filtros.")

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = comisariasFiltradas,
                    key = { it.id }
                ) { comisaria ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = comisaria.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Región: ${comisaria.region}"
                            )

                            Text(
                                text = "Distrito: ${comisaria.distrito}"
                            )

                            Text(
                                text = "Tipo: ${comisaria.tipo}"
                            )

                            Text(
                                text = "Dirección: ${comisaria.direccion}"
                            )

                            Text(
                                text = "Teléfono: ${comisaria.telefono}"
                            )

                            Text(
                                text = "Distancia: ${comisaria.distancia} km"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FiltroDropdown(
    opciones: List<String>,
    seleccion: String,
    onSeleccion: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedButton(
            onClick = {
                expandido = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(seleccion)
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = {
                expandido = false
            }
        ) {

            opciones.forEach { opcion ->

                DropdownMenuItem(
                    text = {
                        Text(opcion)
                    },
                    onClick = {
                        onSeleccion(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}