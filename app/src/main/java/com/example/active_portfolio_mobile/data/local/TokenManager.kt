package com.example.active_portfolio_mobile.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.active_portfolio_mobile.data.remote.dto.User
import kotlinx.serialization.json.Json

class TokenManager (context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER = "user_data"
    }

    fun saveToken(token: String){
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String?{
        return prefs.getString(KEY_TOKEN, null)
    }
    fun saveUser(user: User) {
        val json = Json.encodeToString(user)
        prefs.edit().putString(KEY_USER, json).apply()
    }

    fun getUser(): User? {
        val userJson = prefs.getString(KEY_USER, null) ?: return null
        return Json.decodeFromString(userJson)
    }
    fun clearAll(){
        prefs.edit().clear().apply()
    }
    fun isLoggedIn(): Boolean{
        return getToken() !=null
    }
}

