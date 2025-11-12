package com.example.active_portfolio_mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Portfolio(
    @SerialName("_id")
    val id: String,
    var title: String,
    val createdBy: String,
    val shareToken: String? = null,
    var description: String? = null,
    var visibility: String
)

@Serializable
data class CreatePortfolioRequest(
    val title: String,
    val userId: String,
    val description: String,
    val visibility: String
)

@Serializable
data class UpdatePortfolioRequest(
    val newTitle: String,
    val newDescription: String? = null,
    val newVisibility : String
)

@Serializable
data class DeleteResponse(
    val success: Boolean
)
