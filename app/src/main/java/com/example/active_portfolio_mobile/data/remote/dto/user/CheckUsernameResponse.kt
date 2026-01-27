package com.example.active_portfolio_mobile.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class CheckUsernameResponse(
    val available: Boolean,
    val message: String
)