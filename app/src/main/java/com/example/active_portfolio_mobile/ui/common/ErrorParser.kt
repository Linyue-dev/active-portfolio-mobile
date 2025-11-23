package com.example.active_portfolio_mobile.ui.common

import org.json.JSONObject
import retrofit2.HttpException

/**
 * Parses HTTP error responses and converts them into user-friendly error messages.
 *
 * Extracts the errorMessage field from the response body and maps common error patterns
 * to localized, readable messages for display in the UI.
 *
 * @param ex The HttpException containing the error response
 * @return A user-friendly error message string
 */
object ErrorParser{
    fun errorHttpError(ex: HttpException) : String{
        val errorBody = ex.response()?.errorBody()?.string()
        return try {
            val json = JSONObject(errorBody ?: "")
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
                fullMessage.contains("already token",  ignoreCase = true) ->
                    "The username is already token"
                else ->
                    "Something went wrong. Please try again."
            }
        } catch (e: Exception) {
            "Unexpected error"
        }
    }
}