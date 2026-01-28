package com.example.active_portfolio_mobile.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val program: String?,
    val password: String,
    val username: String
)
