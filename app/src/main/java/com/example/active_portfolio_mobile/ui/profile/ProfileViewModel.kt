package com.example.active_portfolio_mobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserApiService
import com.example.active_portfolio_mobile.data.remote.dto.User
import com.example.active_portfolio_mobile.ui.common.ErrorParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? =null,
    val error: String? = null
)

class ProfileViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val apiService : UserApiService =
        RetrofitClient.createService(UserApiService::class.java, tokenManager)
    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {

        // load cached user from token
        val cached = tokenManager.getUser()
        if (cached != null){
            _uiState.value = _uiState.value.copy(user = cached)
        }
        // fetch lastest user from backend
        getMyProfile()
    }

    /**
     * Clear error message from UI state
     */
    fun cleanError(){
        _uiState.value = _uiState.value.copy(error = null)
    }

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

    fun getUserById(){

    }
}