package com.example.active_portfolio_mobile.model

import kotlinx.serialization.Serializable


@Serializable
data class AdventureSection(
    var label: String,
    val type: String,
    var content: String,
    val adventureId: String,
    var portfolios: List<String>,
    var description: String?
)

@Serializable
data class AdventureSectionText(
    val label: String,
    val content: String,
    val adventureId: String,
    val portfolios: List<String>
)

@Serializable
data class AdventureSectionLink(
    val label: String,
    val content: String,
    val description: String,
    val adventureId: String,
    val portfolios: List<String>
)