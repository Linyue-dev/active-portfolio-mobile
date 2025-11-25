package com.example.active_portfolio_mobile.data.remote


import com.example.active_portfolio_mobile.data.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

/**
 * RetrofitClient
 * This object provides the Retrofit instance used to communicate with the backend server.
 *  It configures:
 *   1. Base URL for development
 *   2. JSON converter using Kotlin Serialization
 *   3. OkHttp client with:
 *    - Token interceptor (to attach Authorization Bearer token)
 *    - Logging interceptor (for debugging network requests)
 *  Usage:
 *   val userApi = RetrofitClient.createService(UserApiService::class.java, tokenManager)
 *   After calling this method, the returned service instance can perform network requests.
 */
object RetrofitClient {
    // Base URL for Android emulator to access local development backend
    private const val DEV_BASE_URL = "http://10.0.2.2:3000/"
    private const val PROD_BASE_URL = "https://activeportfolio.onrender.com/"
//    private const val DEV_BASE_URL = "http://10.0.2.2:1339/"

    private const val BASE_URL = PROD_BASE_URL

    // JSON configuration for Kotlinx Serialization
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val contentType = "application/json".toMediaType()

    // Enable logging to show request & response body in Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     *  Create OkHttp client with authentication & logging support
     */
    private  fun getClient(tokenManager: TokenManager) =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(logging)
            .build()


    /**
     * Creates a Retrofit service instance for authenticated API calls.
     *
     * This method configures Retrofit with:
     * - A base URL
     * - A custom OkHttpClient that injects authentication headers via [TokenManager]
     * - A JSON converter using Kotlinx Serialization
     *
     * @param T The Retrofit service interface type.
     * @param service The service interface class to generate.
     * @param tokenManager Provides access and refresh tokens for authenticated requests.
     *
     * @return An implementation of the requested Retrofit service.
     */

    fun <T> createService (service: Class<T>, tokenManager: TokenManager) :T{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(tokenManager))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(service)
    }
    /**
     * Creates a Retrofit service instance for public (unauthenticated) API calls.
     *
     * This version uses:
     * - A base URL
     * - A simple OkHttpClient with logging enabled
     * - A JSON converter using Kotlinx Serialization
     *
     * @param T The Retrofit service interface type.
     * @param service The service interface class to generate.
     *
     * @return An implementation of the requested Retrofit service.
     */

    fun <T> createPublicService(service: Class<T>): T {
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