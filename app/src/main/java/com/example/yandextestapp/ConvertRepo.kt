package com.example.yandextestapp

import com.example.yandextestapp.entities.Currency
import io.reactivex.rxjava3.core.Single

interface ConvertRepo {

    fun convert(amount: Float, curFrom: String, curTo: String): Single<Float>

    fun getCurrencies(): Single<List<Currency>>
}
