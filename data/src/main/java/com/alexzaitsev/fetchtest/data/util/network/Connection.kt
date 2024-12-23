package com.alexzaitsev.fetchtest.data.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

val Context.isConnected: Boolean
    get() {
        val connectivityManager = connectivityManager ?: return false
        return connectivityManager.allNetworks.any { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NET_CAPABILITY_INTERNET)
                ?: false
        }
    }

fun Context.connectionAsFlow() = callbackFlow {
    connectivityManager?.let { connectManager ->
        val callback = networkCallback { connectionState -> trySend(connectionState) }

        // in case of connecting to several networks at the same time (e.g. WiFi and CELLULAR)
        // subscribe to status for the one that was selected by Android OS as default one
        // allows to avoid issue with offline mode when connection for one of them is lost
        // but another is still connected
        connectManager.registerDefaultNetworkCallback(callback)

        // Remove callback when not used
        awaitClose {
            connectManager.unregisterNetworkCallback(callback)
        }
    }
}

private fun networkCallback(callback: (Boolean) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            // check if there is Internet connection and that it was successfully detected
            val hasInternet = networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET)
            val hasValidated = networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED)
            callback(hasInternet && hasValidated)
        }

        override fun onLost(network: Network) {
            callback(false)
        }
    }
}
