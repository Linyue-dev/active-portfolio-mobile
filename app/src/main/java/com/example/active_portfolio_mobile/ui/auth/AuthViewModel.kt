package com.example.active_portfolio_mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserApiService
import com.example.active_portfolio_mobile.data.remote.dto.LogInRequest
import com.example.active_portfolio_mobile.data.remote.dto.SignUpRequest
import com.example.active_portfolio_mobile.data.remote.dto.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
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
                _uiState.update {
                    it.copy(isLoading = false, error = parseErrorMessage(ex))
                }
            } catch (ex: IOException){
                _uiState.update {
                    it.copy(isLoading = false,error = "Network error. Please check your connection.")
                }
            } catch (ex: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error"
                )
            }
        }
    }
    /**
     * Handle user signup with provided details
     */
    fun signup(firstName: String,lastName: String,email: String,program: String,password: String){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = apiService.signup(SignUpRequest(firstName,lastName,email,program,password))
                tokenManager.saveToken(response.token)
                tokenManager.saveUser(response.user)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    user = response.user
                )
            } catch (ex: HttpException) {
                _uiState.update {
                    it.copy(isLoading = false, error = parseErrorMessage(ex))
                }
            } catch (ex: IOException){
                _uiState.update {
                    it.copy(isLoading = false,error = "Network error. Please check your connection.")
                }
            } catch (ex: Exception){
                _uiState.update {
                    it.copy(isLoading = false, error = "Unexpected error occurred.")
                }
            }
        }
    }
    /**
     * Handle user logout
     */
    fun logout(){
        tokenManager.clearAll()
        _uiState.value = AuthUiState(isLoading = false)
    }

    /**
     * Parses HTTP error response and extracts user-friendly error message.
     * Converts backend error responses into clean, readable messages by:
     * - Extracting the errorMessage field from JSON response body
     * - Filtering out technical details and system error prefixes
     * - Matching specific error patterns to return concise messages
     */
    private fun parseErrorMessage(ex: HttpException):String{
        val errorBody = ex.response()?.errorBody()?.string()
        return try{
            val json = JSONObject(errorBody ?: "")
            val fullMessage = json.optString("errorMessage", "Unexpected error")

            when{
                fullMessage.contains("Invalid email", ignoreCase = true) -> "Invalid email"
                fullMessage.contains("email already exists", ignoreCase = true) -> "Email already exists"
                fullMessage.contains("Invalid password", ignoreCase = true) -> "Invalid password"
                fullMessage.contains("User not found", ignoreCase = true) -> "User not found"
                else -> "Something went wrong. Please try again."
            }
        }catch (e: Exception) {
            "Unexpected error"
        }
    }
}