package com.example.active_portfolio_mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.api.AuthApiService
import com.example.active_portfolio_mobile.data.remote.network.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserPrivateApiService
import com.example.active_portfolio_mobile.data.remote.dto.auth.LoginRequest
import com.example.active_portfolio_mobile.data.remote.dto.user.SignUpRequest
import com.example.active_portfolio_mobile.data.remote.dto.user.User
import com.example.active_portfolio_mobile.domain.repository.AuthRepository
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
    val token: String? = null,
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
    private val authRepository: AuthRepository
) : ViewModel() {

    // Authentication API - for login, logout, token refresh
    private val authApi: AuthApiService = RetrofitClient.authApi

    // User API - for signup (creates new user account)
    private val userPrivateApi: UserPrivateApiService = RetrofitClient.userPrivateApi

    // Initialize to the state of not being logged in
    private val _uiState = MutableStateFlow(
        AuthUiState(isLoggedIn = false)
    )
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * On initialization:
     * - Check if token exists.
     * - If so, restore user and mark state as logged-in.
     */
    init {
        // check state of login in coroutine
        viewModelScope.launch {
            val token = authRepository.getTokenOrNull()
            if (token != null){
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = true,
                    token = token,
                    user = authRepository.getUserOrNull()
                )
            }
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
                // Changed: Use authApi instead of userPrivateApi
                val response = authApi.login(LoginRequest(email, password))

                authRepository.saveToken(response.token)
                authRepository.saveUser(response.user)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    token = response.token,
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
                ex.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = ex.message ?: ex.toString()
                )
            }
        }
    }
    /**
     * Handle user signup with provided details
     */
    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        program: String,
        password: String,
        username:String
    ){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Signup stays in userPrivateApi
                val response = userPrivateApi.signup(
                    SignUpRequest(firstName,lastName,email,program,password, username)
                )
                authRepository.saveToken(response.token)
                authRepository.saveUser(response.user)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    token = response.token,
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
        viewModelScope.launch {
            authRepository.clearAll()
            _uiState.value = AuthUiState(
                isLoading = false,
                isLoggedIn = false,
                user = null,
                token = null,
                error = null
            )
        }
    }
}