package com.example.active_portfolio_mobile.data.remote.network

import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.api.AuthApiService
import com.example.active_portfolio_mobile.data.remote.api.UserPrivateApiService
import com.example.active_portfolio_mobile.data.remote.api.UserPublicApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * RetrofitClient
 * Provides singleton Retrofit service instances for API communication.
 *
 * Must call initialize() before accessing any API services.
 *
 * Usage:
 *   // In Application class:
 *   RetrofitClient.initialize(tokenManager)
 *
 *   // In ViewModels:
 *   val authApi = RetrofitClient.authApi
 *   val userPrivateApi = RetrofitClient.userPrivateApi
 */
object RetrofitClient {
    // Base URLs
    private const val DEV_BASE_URL = "http://10.0.2.2:3000/"
    private const val PROD_BASE_URL = "https://activeportfolio.onrender.com/"
    private const val BASE_URL = DEV_BASE_URL

    // JSON configuration
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    private val contentType = "application/json".toMediaType()

    // Logging interceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // TokenManager - must be initialized
    private lateinit var tokenManager: TokenManager

    /**
     * Initialize RetrofitClient with TokenManager.
     * Must be called before accessing any API services.
     *
     * @param tokenManager Provides authentication tokens.
     */
    fun initialize(tokenManager: TokenManager) {
        this.tokenManager = tokenManager
    }

    // ==================== API Services ====================

    /**
     * Authentication API Service
     * Handles login, logout, token refresh, and session management.
     */
    val authApi: AuthApiService by lazy {
        createService(AuthApiService::class.java, tokenManager)
    }

    /**
     * User Private API Service
     * Handles authenticated user operations (profile updates, password changes).
     * Requires authentication token.
     */
    val userPrivateApi: UserPrivateApiService by lazy {
        createService(UserPrivateApiService::class.java, tokenManager)
    }

    /**
     * User Public API Service
     * Handles public user operations (search, profile viewing).
     * No authentication required.
     */
    val userPublicApi: UserPublicApiService by lazy {
        createPublicService(UserPublicApiService::class.java)
    }

    // ==================== Private Helper Methods ====================

    /**
     * Creates OkHttp client with authentication & logging support.
     */
    private fun getClient(tokenManager: TokenManager) =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(logging)
            .build()

    /**
     * Creates a Retrofit service instance for authenticated API calls.
     */
    private fun <T> createService(service: Class<T>, tokenManager: TokenManager): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(tokenManager))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(service)
    }

    /**
     * Creates a Retrofit service instance for public (unauthenticated) API calls.
     */
    private fun <T> createPublicService(service: Class<T>): T {
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(service)
    }
}