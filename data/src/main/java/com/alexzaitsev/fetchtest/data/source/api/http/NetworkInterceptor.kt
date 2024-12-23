package com.alexzaitsev.fetchtest.data.source.api.http

import com.alexzaitsev.fetchtest.data.util.network.NetworkState
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(private val networkState: NetworkState) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (networkState.isNetwork) {
            return chain.proceed(request)
        } else {
            throw IOException("Network error")
        }
    }
}
