package com.alexzaitsev.fetchtest.data

import com.alexzaitsev.fetchtest.data.repository.ItemsRepository
import com.alexzaitsev.fetchtest.data.source.api.ItemsApi
import com.alexzaitsev.fetchtest.data.source.api.http.NetworkInterceptor
import com.alexzaitsev.fetchtest.data.util.network.NetworkState
import com.alexzaitsev.fetchtest.data.util.network.isConnected
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single {
        NetworkState(androidContext().isConnected)
    }
    single {
        val networkState = get<NetworkState>()
        val client = OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor(networkState))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(ItemsApi::class.java)
    }
    singleOf(::ItemsRepository)
}
