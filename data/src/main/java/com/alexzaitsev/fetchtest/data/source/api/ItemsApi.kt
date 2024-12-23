package com.alexzaitsev.fetchtest.data.source.api

import com.alexzaitsev.fetchtest.data.model.Item
import retrofit2.Response
import retrofit2.http.GET

interface ItemsApi {

    @GET("hiring.json")
    suspend fun getItems(): Response<List<Item>>
}
