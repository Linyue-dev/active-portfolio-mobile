package com.example.active_portfolio_mobile.data.remote.api

import com.example.active_portfolio_mobile.data.remote.dto.user.CheckUsernameResponse
import com.example.active_portfolio_mobile.data.remote.dto.user.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * User Public API Service
 * All endpoints are publicly accessible without authentication.
 * Used for user search, profile viewing, and username availability checks.
 */
interface UserPublicApiService {
    /**
     * Fetch another user's public profile by ID or username.
     *
     * @param identifier The target user's ID or username.
     * @return The user's profile if found.
     */
    @GET("users/{identifier}")
    suspend fun getUserByIdentifier(@Path("identifier") identifier: String) : User

    /**
     * Search users by username (partial match)
     *
     * @param query Search query string (minimum 2 characters)
     * @return List of users matching the query (max 10 result)
     */
    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String) : List<User>

    /**
     * Check if a username is available
     *
     * @param username Username to check
     * @return Response indicate available
     */
    @GET("users/check-username")
    suspend fun checkUsernameAvailability(@Query("username") username: String): CheckUsernameResponse
}