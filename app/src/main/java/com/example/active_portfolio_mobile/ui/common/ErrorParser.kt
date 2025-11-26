package com.example.active_portfolio_mobile.ui.common

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

/**
 * Parses HTTP error responses into user-friendly messages.
 *
 * Supports both HttpException and raw error body strings.
 */
object ErrorParser{
    /**
     * Parses error body JSON and returns user-friendly message.
     *
     * @param errorBody Raw JSON error string from server.
     * @return User-friendly error message.
     */
    fun parseErrorBody(errorBody: String) : String{
        return try {
            if(errorBody.isEmpty()){
                return "Server error - no details available"
            }
            val json = JSONObject(errorBody)
            val fullMessage = json.optString("errorMessage", "Unexpected error")
            when {
                fullMessage.contains("already exists", ignoreCase = true) ->
                    "This email is already registered"
                fullMessage.contains("Invalid email",ignoreCase = true) ->
                    "Please enter a valid email address"
                fullMessage.contains("User not found", ignoreCase = true) ->
                    "User not found"
                fullMessage.contains("password is incorrect", ignoreCase = true) ->
                    "Current password is incorrect"
                fullMessage.contains("password does not meet",  ignoreCase = true) ->
                    "Password must be at least 6 characters"
                fullMessage.contains("must be different",  ignoreCase = true) ->
                    "New password must be different from current password"
                fullMessage.contains("already taken",  ignoreCase = true) ->
                    "The username is already taken"
                fullMessage.contains("Invalid username format", ignoreCase = true) ->
                    "Invalid username format"
                else ->
                    "Something went wrong. Please try again."
            }
        } catch (e: Exception) {
            Log.d("ErrorParser","Parse error: ${e.message}")
            "Unexpected error"
        }
    }

    /**
     * Parses error from HttpException.
     *
     * @param ex HttpException from Retrofit.
     * @return User-friendly error message.
     */
    fun errorHttpError(ex: HttpException): String {
        val errorBody = ex.response()?.errorBody()?.string()
        return parseErrorBody(errorBody ?: "")
    }

    /**
     * Parses error from raw error body string.
     *
     * @param errorBody Raw error body string.
     * @return User-friendly error message.
     */
    fun errorHttpError(errorBody: String): String {
        return parseErrorBody(errorBody)
    }

}

