package com.example.active_portfolio_mobile.data.remote


import com.example.active_portfolio_mobile.model.LogInRequest
import com.example.active_portfolio_mobile.model.LogInResponse
import com.example.active_portfolio_mobile.model.SignUpRequest
import com.example.active_portfolio_mobile.model.User
import retrofit2.http.*

interface UserApiService {
    @POST("users/login")
    suspend fun login(@Body request: LogInRequest) : LogInResponse

    @POST("users")
    suspend fun signUp(@Body request: SignUpRequest) : User
}