package com.example.active_portfolio_mobile.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val program: String? = null,
)