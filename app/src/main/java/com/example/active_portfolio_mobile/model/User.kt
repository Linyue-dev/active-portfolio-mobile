package com.example.active_portfolio_mobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class User(
    @SerialName(value = "_id")
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

@Serializable
data class LogInRequest(
    val email: String,
    val password: String,
)

@Serializable
data class LogInResponse(
    val token: String,
    val user: User
)

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val program: String,
)