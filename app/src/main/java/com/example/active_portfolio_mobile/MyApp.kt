package com.example.active_portfolio_mobile

import android.app.Application
import android.util.Log
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.network.RetrofitClient
/**
 * Application class for Active Portfolio Mobile.
 *
 * Initializes global dependencies:
 * - TokenManager (for JWT token storage)
 * - RetrofitClient (for API communication)
 *
 * This class is instantiated before any other component when the app starts.
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Log.d("MyApp", "Application onCreate called!")
        // Initialize TokenManager
        val tokenManager = TokenManager(this)

        // Initialize RetrofitClient with TokenManager
        RetrofitClient.initialize(tokenManager)

        Log.d("MyApp", "RetrofitClient initialized!")
    }
}