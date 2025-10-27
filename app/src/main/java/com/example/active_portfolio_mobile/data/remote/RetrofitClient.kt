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

    private const val BASE_URL = DEV_BASE_URL

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
     * Generic function to build Retrofit service interface
     */
    fun <T> createService (service: Class<T>, tokenManager: TokenManager) :T{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(tokenManager))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(service)
    }
}