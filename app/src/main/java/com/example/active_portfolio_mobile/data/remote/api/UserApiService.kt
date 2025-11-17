package com.example.active_portfolio_mobile.data.remote.api

import com.example.active_portfolio_mobile.data.remote.dto.ChangePasswordRequest
import com.example.active_portfolio_mobile.data.remote.dto.ChangePasswordResponse
import com.example.active_portfolio_mobile.data.remote.dto.LogInRequest
import com.example.active_portfolio_mobile.data.remote.dto.LogInResponse
import com.example.active_portfolio_mobile.data.remote.dto.SignUpRequest
import com.example.active_portfolio_mobile.data.remote.dto.SignUpResponse
import com.example.active_portfolio_mobile.data.remote.dto.UpdateUserRequest
import com.example.active_portfolio_mobile.data.remote.dto.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
/**
 * API endpoints for user authentication and profile operations.
 */
interface UserApiService {
    /**
     * Authenticate a user with email and password.
     *
     * @param request Login credentials.
     * @return Contains JWT token and user profile data.
     */
    @POST("users/login")
    suspend fun login(@Body request: LogInRequest) : LogInResponse

    /**
     * Register a new user account.
     *
     * @param request User registration details.
     * @return Contains JWT token and created user profile.
     */
    @POST("users")
    suspend fun signup(@Body request: SignUpRequest) : SignUpResponse

    /**
     * Fetch the authenticated user's profile.
     *
     * Requires a valid JWT token. Returns full user information.
     */
    @GET("users/me")
    suspend fun getCurrentUser() : User

    /**
     * Fetch another user's public profile by ID.
     *
     * @param id The target user's ID.
     * @return The user's profile if found.
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String) : User

    /**
     * Request body for updating user profile.
     * Only non-null fields will be updated by the backend.
     */
    @PATCH("users/me")
    suspend fun updateUser(@Body updateData: UpdateUserRequest) : User

    /**
     * Changes the authenticated user's password.
     */
    @PATCH("/users/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest) : ChangePasswordResponse
}