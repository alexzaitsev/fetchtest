package com.alexzaitsev.fetchtest.data.util.network

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.failure
import retrofit2.Response

internal suspend fun <T> request(request: suspend () -> Response<T>): Result<T, Exception> =
    try {
        request().asResult()
    } catch (ex: Exception) {
        ex.failure()
    }
