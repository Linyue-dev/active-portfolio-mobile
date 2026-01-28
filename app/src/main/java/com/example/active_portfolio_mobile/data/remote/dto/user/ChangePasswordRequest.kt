package com.example.active_portfolio_mobile.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
