package com.example.active_portfolio_mobile.data.remote.repository

import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.dto.user.User
import com.example.active_portfolio_mobile.domain.repository.AuthRepository

/**
 * Implementation of [AuthRepository] responsible for managing
 * authentication-related data such as user tokens and user information.
 *
 * This class delegates actual storage operations to [TokenManager],
 * allowing the domain layer to remain independent of storage details.
 *
 * Responsibilities:
 * - Persist authentication token after login
 * - Retrieve stored authentication token
 * - Persist authenticated user information
 * - Retrieve stored user information
 * - Clear all authentication data during logout or session reset
 *
 * This separation allows storage implementation (e.g., DataStore,
 * encrypted storage, or database) to change without affecting
 * higher application layers.
 */
class AuthRepositoryImpl (
    private val tokenManager: TokenManager
) : AuthRepository{

    /** Saves authentication token for later authorized requests. */
    override suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }
    /** Returns stored authentication token or null if unavailable. */
    override suspend fun getTokenOrNull(): String? {
        return tokenManager.getToken()
    }
    /** Stores authenticated user information locally. */
    override suspend fun saveUser(user: User) {
        tokenManager.saveUser(user)
    }
    /** Returns stored user information or null if unavailable. */
    override suspend fun getUserOrNull(): User? {
        return tokenManager.getUser()
    }
    /** Clears all authentication-related stored data. */
    override suspend fun clearAll() {
        tokenManager.clearAll()
    }
}