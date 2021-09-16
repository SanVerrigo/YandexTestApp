package com.example.yandextestapp

import android.content.Context
import com.example.yandextestapp.entities.ConvertResult
import com.example.yandextestapp.entities.Currency
import io.reactivex.rxjava3.core.Single

class ConvertRepoImpl(context: Context) : ConvertRepo {

    private val converter = ConverterService(context)

    override fun convert(amount: Float, curFrom: String, curTo: String): Single<Float> {
        return converter.service.convert(amount, curFrom, curTo)
            .map(ConvertResult::rate)
    }

    override fun getCurrencies(): Single<List<Currency>> {
        return converter.service.getCurrencies().map { content ->
            content.asJsonObject.entrySet().map { (code, name) ->
                Currency(code, name.asString)
            }
        }
    }

}
