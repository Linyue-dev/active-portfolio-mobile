package com.example.active_portfolio_mobile.ui.common

import retrofit2.HttpException
import org.json.JSONObject

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
                else ->
                    "Something went wrong. Please try again."
            }
        } catch (e: Exception) {
            "Unexpected error"
        }
    }
}