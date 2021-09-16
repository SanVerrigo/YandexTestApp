package com.example.yandextestapp

import android.content.Context
import io.reactivex.rxjava3.core.Single

class ConvertRepoImpl(context: Context) : ConvertRepo {

    private val converter = ConverterService(context)

    override fun convert(amount: Float, curFrom: String, curTo: String): Single<Float> {
        return converter.service.convert(amount, curFrom, curTo).map { jsonEl ->
            jsonEl.asJsonObject["rates"].asJsonObject[curTo].asFloat
        }
    }

    override fun getCurrencies(): Single<List<String>> {
        return converter.service.getCurrencies().map { jsonEl ->
            jsonEl.asJsonObject.keySet().toList()
        }
    }

}
