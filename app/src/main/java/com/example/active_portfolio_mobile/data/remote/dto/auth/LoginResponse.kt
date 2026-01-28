package com.example.active_portfolio_mobile.data.remote.dto.auth

import com.example.active_portfolio_mobile.data.remote.dto.user.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: User
)
