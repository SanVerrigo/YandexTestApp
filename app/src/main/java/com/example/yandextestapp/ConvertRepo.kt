package com.example.yandextestapp

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ConvertRepo {

    fun convert(amount: Float, curFrom: String, curTo: String): Single<Float>

    fun getCurrencies(): Single<List<String>>
}
