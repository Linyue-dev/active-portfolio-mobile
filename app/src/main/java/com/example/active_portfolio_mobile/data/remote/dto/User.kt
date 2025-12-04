package com.example.active_portfolio_mobile.data.remote.dto

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
    val firstName: String,
    val lastName: String,
    val email: String,
    val program: String?,
    val password: String,
    val username: String
)

@Serializable
data class SignUpResponse(
    val user: User,
    val token: String
)

@Serializable
data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val program: String? = null,
)

@Serializable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

@Serializable
data class ChangePasswordResponse(
    val message: String
)

@Serializable
data class CheckUsernameResponse(
    val available: Boolean,
    val message: String
)