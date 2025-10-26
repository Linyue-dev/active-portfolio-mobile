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
)