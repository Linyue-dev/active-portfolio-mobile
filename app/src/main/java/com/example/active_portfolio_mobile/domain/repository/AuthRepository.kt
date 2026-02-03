package com.example.active_portfolio_mobile.domain.repository

import com.example.active_portfolio_mobile.data.remote.dto.user.User
/**
 * Repository interface responsible for managing authentication state
 * within the application.
 *
 * This interface abstracts how authentication data is stored and retrieved,
 * allowing the rest of the application to remain independent from the
 * underlying storage implementation.
 *
 * Responsibilities include:
 * - Persisting authentication tokens
 * - Retrieving stored tokens
 * - Persisting authenticated user information
 * - Retrieving stored user information
 * - Clearing authentication-related data on logout or session reset
 */
interface AuthRepository {
    /** Saves authentication token for future authorized requests. */
    suspend fun saveToken(token: String)

    /** Returns stored authentication token or null if unavailable. */
    suspend fun getTokenOrNull(): String?

    /** Stores authenticated user information locally. */
    suspend fun saveUser(user: User)

    /** Returns stored user information or null if unavailable. */
    suspend fun getUserOrNull(): User?

    /** Clears both token and user information from storage. */
    suspend fun clearAll()
}