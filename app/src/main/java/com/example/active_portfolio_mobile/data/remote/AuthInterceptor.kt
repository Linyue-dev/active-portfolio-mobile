package com.example.active_portfolio_mobile.data.remote

import android.util.Log
import com.example.active_portfolio_mobile.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

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