package com.alexzaitsev.fetchtest.ui.component.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alexzaitsev.fetchtest.data.util.network.NetworkState
import com.alexzaitsev.fetchtest.data.util.network.connectionAsFlow
import org.koin.compose.koinInject

@Composable
fun ObserveNetworkState() {
    val networkState: NetworkState = koinInject()
    val context = LocalContext.current
    OneTime {
        context.connectionAsFlow().collect { networkState.onIsNetworkChanged(it) }
    }
}
