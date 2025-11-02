package com.example.active_portfolio_mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AdventureSection(
    @SerialName(value = "_id")
    val id: String,
    val label: String,
    val type: String,
    val content: String,
    val adventureId: String,
    val portfolios: List<String>,
    val description: String? = ""
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