package com.example.yandextestapp.entities.deserializers

import com.example.yandextestapp.entities.ConvertResult
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ConvertResultDeserializer : JsonDeserializer<ConvertResult> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConvertResult {
        return ConvertResult(
            json.asJsonObject["rates"].asJsonObject.entrySet().first().value.asFloat
        )
    }
}
