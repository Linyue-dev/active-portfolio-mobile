package com.example.active_portfolio_mobile.data.remote.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive


@Serializable
data class AdventureSection(
    @SerialName(value = "_id")
    val id: String,
    val label: String,
    val type: String,
    val adventureId: String,
    val portfolios: List<String>,
    val description: String? = "",

    // If content is a list of strings (in case of images or other files), treat it as a single
    // string for now and parse it later as needed.
    @Serializable(with = FlexibleStringSerializer::class)
    val content: String
)

@Serializable
data class AdventureSectionUpdateRequest(
    val newLabel: String,
    val newContentString: String,
    val newDescription: String = "",
    val newPortfolios: List<String>
)


object SectionType {
    const val TEXT = "text"
    const val LINK = "link"
    const val IMAGE = "image"
}

/**
 * A custom serializer which serializes both strings and lists of strings into simple strings
 * for specified properties. The "stringified" lists can then be parsed later on as needed using
 * JSONArray(rawContent)
 */
object FlexibleStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FlexibleString", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): String {
        val input = decoder as? JsonDecoder ?: error("FlexibleStringSerializer works only with JsonDecoder")
        val element = input.decodeJsonElement()

        return when (element) {
            is JsonArray -> element.toString()
            is JsonPrimitive -> element.content
            else -> error("Unexpected JSON type for content")
        }
    }
    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
}
