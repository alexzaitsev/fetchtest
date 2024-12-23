package com.alexzaitsev.fetchtest.data.util.network

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import retrofit2.Response

fun <T> Response<T>.asResult(): Result<T, Exception> =
    if (isSuccessful) {
        body()?.success()?: NullPointerException("body is null").failure()
    } else {
        Exception(errorBody()?.string()).failure()
    }
