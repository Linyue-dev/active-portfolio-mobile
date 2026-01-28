package com.example.active_portfolio_mobile.data.remote.api

import com.example.active_portfolio_mobile.data.remote.dto.auth.LoginRequest
import com.example.active_portfolio_mobile.data.remote.dto.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Authentication API Service
 * Handles all authentication-related operations.
 */
interface AuthApiService {
    /**
     * Authenticate a user with email and password.
     *
     * @param request Login credentials (email and password).
     * @return Contains JWT token and user profile data.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /**
     * Fetch the currently authenticated user's profile.
     *
     * Requires authentication token (Bearer or Cookie).
     * Returns complete user information along with token.
     *
     * @return User profile data with token.
     */
    @GET("auth/me")
    suspend fun getCurrentUser(): LoginResponse

    /**
     * Refresh the authentication token to extend the session.
     *
     * Requires a valid token. Returns new token with extended expiration.
     *
     * @return New JWT token and updated user data.
     */
    @POST("auth/refresh")
    suspend fun refreshToken(): LoginResponse

    /**
     * Log out the current user.
     *
     * Clears authentication cookie on web platform.
     * Mobile/Desktop clients should discard their stored token after calling this.
     *
     * @return Success message.
     */
    @POST("auth/logout")
    suspend fun logout(): Unit
}