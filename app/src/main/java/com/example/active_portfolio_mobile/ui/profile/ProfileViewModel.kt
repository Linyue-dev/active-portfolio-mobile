package com.example.active_portfolio_mobile.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserApiService
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
 * UI state for profile-related screens.
 *
 * @property isLoading Indicates whether a profile request is in progress.
 * @property user      The current user profile, or null while loading.
 * @property error     Error message for profile operations, null when no error.
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? =null,
    val error: String? = null
)
/**
 * ViewModel responsible for loading and updating user profile data.
 *
 * Responsibilities:
 * - Keep cached user data in TokenManager synchronized with latest backend state
 * - Update individual profile fields (first name, last name, username, bio, etc.)
 * - Expose reactive UI state via StateFlow
 *
 * Functions:
 * - getMyProfile(): Fetch the latest profile from the backend and update local cache
 * - updateField(): Update a single profile field on the server and refresh local state
 */
class ProfileViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val apiService : UserApiService =
        RetrofitClient.createService(UserApiService::class.java, tokenManager)
    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // fetch lastest user from backend
        getMyProfile()
    }

    /**
     * Load the authenticated user's profile from backend.
     * Also updates local cached user in TokenManager.
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
     * Update a single user profile field.
     *
     * @param field Field name to update.
     * @param value New field value.
     * @param onSuccess Callback invoked after successful update.
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
                    "firstName" -> UpdateUserRequest(firstName = value)
                    "lastName" -> UpdateUserRequest(lastName = value)
                    "username" -> UpdateUserRequest(username = value)
                    "program" -> UpdateUserRequest(program = value)
                    "bio" -> UpdateUserRequest(bio = value)
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
}