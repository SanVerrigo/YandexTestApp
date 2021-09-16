package com.example.yandextestapp

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MyDeserializer : JsonDeserializer<List<Currency>> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Currency>? {
        if (json == null) return null
        val cont = json.asJsonObject
        val arr = ArrayList<Currency>()
        for (each in cont.keySet()) {
            arr.add(Currency(ind = each, name = cont[each].toString()))
        }
        return arr.toList()
    }


}
