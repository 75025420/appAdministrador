package com.example.centralemergenciasadministrador.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class ConnectivityObserver(
    context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    fun observarConexion(): Flow<Boolean> = callbackFlow {

        val callback =
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    trySend(hayConexion())
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    val conectado =
                        networkCapabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_INTERNET
                        )

                    trySend(conectado)
                }
            }

        trySend(hayConexion())

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

    }.distinctUntilChanged()

    private fun hayConexion(): Boolean {

        val redActiva =
            connectivityManager.activeNetwork ?: return false

        val capacidades =
            connectivityManager.getNetworkCapabilities(redActiva)
                ?: return false

        return capacidades.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }
}