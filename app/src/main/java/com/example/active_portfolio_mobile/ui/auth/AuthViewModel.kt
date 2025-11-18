package com.example.active_portfolio_mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserApiService
import com.example.active_portfolio_mobile.data.remote.dto.LogInRequest
import com.example.active_portfolio_mobile.data.remote.dto.SignUpRequest
import com.example.active_portfolio_mobile.data.remote.dto.User
import com.example.active_portfolio_mobile.ui.common.ErrorParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
/**
 * UI state for authentication-related screens.
 *
 * @property isLoading  Indicates whether an authentication request is in progress.
 * @property isLoggedIn True when a valid token/user is stored.
 * @property user       The authenticated user, or null when logged out.
 * @property error      Error message for login/signup failures, null when no error.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user : User? = null,
    val error: String? = null,
)

/**
 * ViewModel responsible for all authentication operations such as login, signup, and logout.
 *
 * Functions:
 * - [login] Authenticate an existing user with email and password.
 * - [signup] Register a new user with provided account information.
 * - [logout] Clear stored credentials and reset authentication state.
 * - [cleanError] Clear error from UI state (called when editing inputs).
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

    /**
     * On initialization:
     * - Check if token exists.
     * - If so, restore user and mark state as logged-in.
     */
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
                    it.copy(isLoading = false, error = ErrorParser.errorHttpError(ex))
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
                    it.copy(isLoading = false, error = ErrorParser.errorHttpError(ex))
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
        _uiState.value = AuthUiState(
            isLoading = false,
            isLoggedIn = false,
            user = null,
            error = null
        )
    }
}