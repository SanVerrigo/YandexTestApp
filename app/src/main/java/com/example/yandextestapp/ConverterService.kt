package com.example.yandextestapp

import android.content.Context
import com.example.yandextestapp.entities.ConvertResult
import com.example.yandextestapp.entities.deserializers.ConvertResultDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConverterService(context: Context) {

    val service: ConverterApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        service = retrofit.create(ConverterApi::class.java)
    }

    private fun createGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ConvertResult::class.java, ConvertResultDeserializer())
            .create()
    }
}
