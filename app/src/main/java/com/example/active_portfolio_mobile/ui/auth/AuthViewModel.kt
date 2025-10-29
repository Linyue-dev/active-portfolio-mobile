package com.example.active_portfolio_mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.UserApiService
import com.example.active_portfolio_mobile.model.LogInRequest
import com.example.active_portfolio_mobile.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

/**
 * Data class to hold authentication UI state
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user : User? = null,
    val error: String? = null,
)

/**
 * ViewModel for handling authentication logic (login, signup, logout)
 */
class AuthViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    // Retrofit API service for backend communication
    private val apiService : UserApiService =
        RetrofitClient.createService(UserApiService::class.java, tokenManager)
    private val _uiState = MutableStateFlow(
        //It will check whether there is already a Token in the TokenManager.
        AuthUiState(isLoggedIn = tokenManager.isLoggedIn())
    )

    val uiState: StateFlow<AuthUiState> = _uiState

    // During initialization, check whether to automatically log in
    init {
        if (tokenManager.isLoggedIn()){
            _uiState.value = _uiState.value.copy(
                isLoggedIn = true,
                user = tokenManager.getUser()
            )
        }
    }

    /**
     * Clear error message from UI state
     */
    fun cleanError(){
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Handle user login with email and password
     */
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = apiService.login(LogInRequest(email, password))

                tokenManager.saveToken(response.token)
                tokenManager.saveUser(response.user)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    user = response.user
                )

            } catch (ex: HttpException) {
                val errorBody = ex.response()?.errorBody()?.string()

                val message = try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("errorMessage", "Invalid email or password")
                } catch (e: Exception) {
                    "Invalid email or password"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = message
                )

            } catch (ex: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error"
                )
            }
        }
    }


    /**
     * Handle user logout
     */
    fun logout(){

        // Clear all token and user data
        tokenManager.clearAll()
        // Reset UI state to initial state
        _uiState.value = AuthUiState(isLoading = false)
    }
}