package com.example.active_portfolio_mobile.data.remote.api

import com.example.active_portfolio_mobile.data.remote.dto.LogInRequest
import com.example.active_portfolio_mobile.data.remote.dto.LogInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("users/login")
    suspend fun login(@Body request: LogInRequest) : LogInResponse
}