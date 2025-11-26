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
import retrofit2.Response

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
     * Requires authentication. Returns complete user information.
     *
     * @return User profile data.
     */
    @GET("users/me")
    suspend fun getCurrentUser() : User

    /**
     * Updates the authenticated user's profile.
     * Only non-null fields will be updated.
     *
     * @param updateData Partial user data to update.
     * @return Response<User> with updated user or error details.
     */
    @PATCH("users/me")
    suspend fun updateUser(@Body updateData: UpdateUserRequest) : Response<User>

    /**
     * Changes the authenticated user's password.
     *
     * Validates old password before updating. Requires authentication.
     *
     * @param request Contains oldPassword and newPassword.
     * @return ChangePasswordResponse confirming the change.
     */
    @PATCH("/users/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest) : ChangePasswordResponse
}