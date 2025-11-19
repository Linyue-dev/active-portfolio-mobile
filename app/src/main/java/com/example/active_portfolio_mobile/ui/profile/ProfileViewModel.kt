package com.example.active_portfolio_mobile.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserApiService
import com.example.active_portfolio_mobile.data.remote.dto.ChangePasswordRequest
import com.example.active_portfolio_mobile.data.remote.dto.UpdateUserRequest
import com.example.active_portfolio_mobile.data.remote.dto.User
import com.example.active_portfolio_mobile.ui.common.ErrorParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for all profile-related screens.
 *
 * @property isLoading  Whether a profile-related request is currently in progress.
 * @property user       The authenticated user's profile, or `null` before fetched.
 * @property error      Error message produced by profile operations, or `null` when there is no error.
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? =null,
    val error: String? = null
)
/**
 * ViewModel responsible for retrieving and updating user profile information.
 *
 * Lifecycle:
 * - On init, loads cached user from [TokenManager] (if exists)
 * - Immediately fetches the latest user profile from the backend to ensure freshness
 *
 * @param tokenManager Handles secure storage of tokens and cached user data
 */
class ProfileViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val apiService : UserApiService =
        RetrofitClient.createService(UserApiService::class.java, tokenManager)
    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val cached = tokenManager.getUser()
        if (cached != null){
            _uiState.value = _uiState.value.copy(user = cached)
        }
        // fetch lastest user from backend
        getMyProfile()
    }

    /**
     * Retrieves the authenticated user's latest profile from the backend.
     *
     * Behavior:
     * - Shows loading indicator in UI
     * - Updates the local cached user stored in [TokenManager]
     * - Updates UI state with fresh user data
     * - Emits human-readable error messages on failure
     *
     * Error Handling:
     * - [HttpException] → parsed into meaningful messages using [ErrorParser]
     */
    fun getMyProfile(){
        val token = tokenManager.getToken()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try{
                val newUser = apiService.getCurrentUser()

                // save to local storage
                tokenManager.saveUser(newUser)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = newUser
                )

            } catch (ex: HttpException){
                _uiState.update {
                    it.copy(isLoading = false, error = ErrorParser.errorHttpError(ex))
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
     * Updates a single field of the user's profile.
     *
     * @param field The profile field name to update (e.g., `"firstName"`, `"username"`, `"bio"`, `"program"`).
     * @param value The new string value for this field. Value is trimmed before sending.
     * @param onSuccess Callback invoked when the update finishes successfully.
     *
     * Error Handling:
     * - Backend errors → converted with [ErrorParser.errorHttpError]
     * - Network errors → “Network error”
     */
    fun updateField(
        field : String,
        value: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val request = when (field){
                    "firstName" -> UpdateUserRequest(firstName = value.trim())
                    "lastName" -> UpdateUserRequest(lastName = value.trim())
                    "username" -> UpdateUserRequest(username = value.trim())
                    "program" -> UpdateUserRequest(program = value.trim())
                    "bio" -> UpdateUserRequest(bio = value.trim())
                    else -> throw IllegalArgumentException("Unknown field: $field")
                }

                val updateUser = apiService.updateUser(request)
                tokenManager.saveUser(updateUser)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
                // go back previous page after save successfully
                onSuccess()

            } catch (ex: HttpException){
                val errorBody = ex.response()?.errorBody()?.string()
                Log.e("ProfileViewModel", "HTTP ${ex.code()}: $errorBody")
                _uiState.update {
                    it.copy( isLoading = false, error = ErrorParser.errorHttpError(ex))
                }
            } catch (ex: Exception){
                _uiState.update {
                    it.copy( isLoading = false, error = "Network error")
                }
            }
        }
    }

    /**
     * Changes the authenticated user's password.
     *
     * @param oldPassword The user's current password (required for verification).
     * @param newPassword The new password to set. Must meet backend validation rules.
     * @param onSuccess   Callback invoked when the password is successfully changed.
     *
     * Error Handling:
     * - [HttpException] → parsed to user-friendly message
     * - Unexpected errors → generic error message
     */
    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try{
                val request = ChangePasswordRequest(
                    oldPassword = oldPassword.trim(),
                    newPassword = newPassword.trim()
                )
                apiService.changePassword(request)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
                onSuccess()

            } catch (ex: HttpException){
                _uiState.update {
                    it.copy(isLoading = false, error = ErrorParser.errorHttpError(ex))
                }
            } catch (ex: Exception){
                _uiState.update {
                    it.copy(isLoading = false, error = "An unexpected error occurred.")
                }
            }
        }
    }
    /**
     * Clears the current error message from the UI state.
     * Used after displaying the error in a Snackbar or dialog.
     */
    fun cleanError(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}