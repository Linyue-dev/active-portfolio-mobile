package com.example.active_portfolio_mobile.data.remote.api

import com.example.active_portfolio_mobile.data.remote.dto.LogInRequest
import com.example.active_portfolio_mobile.data.remote.dto.LogInResponse
import com.example.active_portfolio_mobile.data.remote.dto.SignUpRequest
import com.example.active_portfolio_mobile.data.remote.dto.SignUpResponse
import com.example.active_portfolio_mobile.data.remote.dto.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    // User login
    @POST("users/login")
    suspend fun login(@Body request: LogInRequest) : LogInResponse

    // User registration
    @POST("users")
    suspend fun signup(@Body request: SignUpRequest) : SignUpResponse

    // User edit own profile
    @GET("users/me")
    suspend fun getCurrentUser() : User

    // User take look other User
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String) : User
}