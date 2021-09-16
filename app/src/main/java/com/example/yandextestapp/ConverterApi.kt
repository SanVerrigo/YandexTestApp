package com.example.yandextestapp

import com.example.yandextestapp.entities.ConvertResult
import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterApi {

    @GET("currencies")
    fun getCurrencies(): Single<JsonElement>

    @GET("latest")
    fun convert(
        @Query("amount") amount: Float,
        @Query("from") from: String,
        @Query("to") to: String
    ): Single<ConvertResult>
}
