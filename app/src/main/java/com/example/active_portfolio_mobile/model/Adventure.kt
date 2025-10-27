package com.example.active_portfolio_mobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Adventure(
    @SerialName(value = "_id")
    val id: String,
    val userId: String,
    var title: String,
    var visibility: String,
    var portfolios: List<String>
) {
    override fun toString(): String {
        return title
    }
}

@Serializable
data class AdventureUpdateRequest(
    val newTitle: String,
    val newVisibility: String,
    val newPortfolios: List<String>
)