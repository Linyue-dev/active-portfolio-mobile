package com.example.active_portfolio_mobile.data.remote

import android.util.Log
import com.example.active_portfolio_mobile.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
/**
 * Interceptor that automatically adds JWT authentication token to API requests.
 *
 * This interceptor checks if a valid token exists in TokenManager and attaches it
 * to the Authorization header as a Bearer token for all outgoing requests.
 *
 * @param tokenManager TokenManager instance that stores and retrieves JWT tokens
 *
 * Behavior:
 * - If token exists: Adds "Authorization: Bearer {token}" header
 * - If no token: Proceeds with original request without auth header
 *
 * Used in RetrofitClient to enable authenticated API calls.
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("API", "URL: ${request.url}")
        Log.d("API", "Token: ${tokenManager.getToken()}")
        val token = tokenManager.getToken()

        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}