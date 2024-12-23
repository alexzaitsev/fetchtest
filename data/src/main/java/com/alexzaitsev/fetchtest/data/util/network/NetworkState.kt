package com.alexzaitsev.fetchtest.data.util.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkState(initial: Boolean) {

    private val _isNetworkFlow = MutableStateFlow(initial)

    val isNetworkFlow: StateFlow<Boolean> = _isNetworkFlow

    val isNetwork: Boolean
        get() = _isNetworkFlow.value

    suspend fun onIsNetworkChanged(isNetwork: Boolean) {
        _isNetworkFlow.emit(isNetwork)
    }
}
