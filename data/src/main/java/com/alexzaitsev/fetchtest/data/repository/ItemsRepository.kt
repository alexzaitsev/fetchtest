package com.alexzaitsev.fetchtest.data.repository

import com.alexzaitsev.fetchtest.data.model.Item
import com.alexzaitsev.fetchtest.data.source.api.ItemsApi
import com.alexzaitsev.fetchtest.data.util.network.request
import com.github.kittinunf.result.Result

class ItemsRepository(private val itemsApi: ItemsApi) {

    suspend fun getItems(): Result<List<Item>, Exception> = request { itemsApi.getItems() }
}
