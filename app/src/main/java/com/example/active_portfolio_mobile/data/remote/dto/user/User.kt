package com.example.active_portfolio_mobile.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class User(
    @SerialName(value = "userId")
    val id: String,
    val username: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val program: String? = null,
    val role: String,
    val bio: String? = null,
    val banner: String? = null,
    val profilePicture: String? = null,
)