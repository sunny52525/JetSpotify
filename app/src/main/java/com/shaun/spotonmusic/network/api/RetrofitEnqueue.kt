package com.shaun.spotonmusic.network.api

import retrofit2.Call

class RetrofitEnqueue {

    companion object {

        sealed class Result<T> {
            data class Success<T>(val call: Call<T>, val response: retrofit2.Response<T>) :
                Result<T>()

            data class Failure<T>(val call: Call<T>, val error: Throwable) : Result<T>()
        }


        inline fun <reified T> Call<T>.enqueue(crossinline result: (Result<T>) -> Unit) {
            enqueue(object : retrofit2.Callback<T> {
                override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                    result(Result.Success(call, response))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    result(Result.Failure(call = call, error = t))
                }

            })
        }
    }

}