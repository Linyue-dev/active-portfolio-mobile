package com.example.active_portfolio_mobile.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val user: User,
    val token: String
)
