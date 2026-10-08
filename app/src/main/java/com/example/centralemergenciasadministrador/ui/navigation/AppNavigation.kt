package com.example.centralemergenciasadministrador.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.centralemergenciasadministrador.CentralEmergenciasAdminApplication
import com.example.centralemergenciasadministrador.ui.screens.ComisariasScreen
import com.example.centralemergenciasadministrador.ui.screens.DetailScreen
import com.example.centralemergenciasadministrador.ui.screens.HomeScreen
import com.example.centralemergenciasadministrador.ui.screens.ListScreen
import com.example.centralemergenciasadministrador.ui.screens.LoginScreen
import com.example.centralemergenciasadministrador.ui.screens.SplashScreen
import com.example.centralemergenciasadministrador.ui.screens.SyncScreen
import com.example.centralemergenciasadministrador.viewmodel.AppViewModelFactory
import com.example.centralemergenciasadministrador.viewmodel.AuthViewModel
import com.example.centralemergenciasadministrador.viewmodel.ComisariaViewModel
import com.example.centralemergenciasadministrador.viewmodel.DenunciaViewModel
import com.example.centralemergenciasadministrador.viewmodel.EstadoSesion
import com.example.centralemergenciasadministrador.viewmodel.SyncViewModel
import com.example.centralemergenciasadministrador.worker.SyncWorker

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    val context = LocalContext.current

    val application =
        context.applicationContext as CentralEmergenciasAdminApplication

    val factory = AppViewModelFactory(
        authRepository = application.container.authRepository,
        denunciaRepository = application.container.denunciaRepository,
        comisariaRepository = application.container.comisariaRepository,
        syncRepository = application.container.syncRepository
    )

    val authViewModel: AuthViewModel =
        viewModel(factory = factory)

    val denunciaViewModel: DenunciaViewModel =
        viewModel(factory = factory)

    val comisariaViewModel: ComisariaViewModel =
        viewModel(factory = factory)

    val syncViewModel: SyncViewModel =
        viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {

        // SPLASH SCREEN
        composable("splash") {

            val estadoSesion by
            authViewModel.estadoSesion.collectAsState()

            LaunchedEffect(Unit) {
                authViewModel.verificarSesion()
            }

            LaunchedEffect(estadoSesion) {
                when (estadoSesion) {
                    EstadoSesion.Autenticado -> {
                        navController.navigate("home") {
                            popUpTo("splash") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                    EstadoSesion.SinSesion -> {
                        navController.navigate("login") {
                            popUpTo("splash") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                    EstadoSesion.Verificando -> Unit
                }
            }

            SplashScreen()
        }

        // LOGIN
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,

                onLoginExitoso = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // HOME
        composable("home") {
            HomeScreen(
                onVerDenuncias = {
                    navController.navigate("denuncias")
                },

                onVerComisarias = {
                    navController.navigate("comisarias")
                },

                onVerSincronizacion = {
                    navController.navigate("sincronizacion")
                },

                onCerrarSesion = {
                    authViewModel.cerrarSesion()

                    navController.navigate("login") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        // COMISARÍAS
        composable("comisarias") {
            ComisariasScreen(
                comisariaViewModel = comisariaViewModel
            )
        }

        // LISTADO DE DENUNCIAS
        composable("denuncias") {
            ListScreen(
                denunciaViewModel = denunciaViewModel,

                onVerDetalle = { id ->
                    navController.navigate("detalle/$id")
                }
            )
        }

        // DETALLE DE DENUNCIA
        composable(
            route = "detalle/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getInt("id")
                ?: return@composable

            DetailScreen(
                denunciaId = id,
                denunciaViewModel = denunciaViewModel,

                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        // SINCRONIZACIÓN
        composable("sincronizacion") {
            SyncScreen(
                syncViewModel = syncViewModel,

                onSincronizar = {
                    val restricciones =
                        Constraints.Builder()
                            .setRequiredNetworkType(
                                NetworkType.CONNECTED
                            )
                            .build()

                    val solicitud =
                        OneTimeWorkRequestBuilder<SyncWorker>()
                            .setConstraints(restricciones)
                            .build()

                    WorkManager
                        .getInstance(context)
                        .enqueue(solicitud)
                }
            )
        }
    }
}