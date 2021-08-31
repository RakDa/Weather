package com.strajnsak.weathermastr.data.remote

import com.strajnsak.weathermastr.util.ResultWrapper
import retrofit2.Response

abstract class MasterRemoteDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResultWrapper<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResultWrapper.Success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): ResultWrapper<T> {
        return ResultWrapper.Error("Api call failed - $message")
    }
}