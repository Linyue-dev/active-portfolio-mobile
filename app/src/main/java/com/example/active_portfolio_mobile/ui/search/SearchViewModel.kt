package com.example.active_portfolio_mobile.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.RetrofitClient
import com.example.active_portfolio_mobile.data.remote.api.UserPublicApiService
import com.example.active_portfolio_mobile.data.remote.dto.User
import com.example.active_portfolio_mobile.ui.common.ErrorParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

/**
 * UI state for the user search screen.
 *
 * @property results The list of users matching the current search query.
 * @property isLoading Whether a search request is currently in progress.
 * @property message An optional message for displaying feedback to the user
 *                   (e.g., error messages or “no results” notifications).
 */
data class SearchUiState(
    val results: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

/**
 * ViewModel responsible for handling user search logic.
 *
 * This ViewModel:
 * - Validates the search query before making API calls.
 * - Sends HTTP requests to fetch matching users.
 * - Updates [uiState] with loading, results, and message feedback.
 * - Handles API, network, and unknown exceptions gracefully.
 */
class SearchViewModel(
): ViewModel(){
    private val apiService = RetrofitClient.createPublicService(UserPublicApiService::class.java)
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()


    /**
     * Performs a user search for the given [query].
     *
     * Behavior:
     * - Blank or too-short queries immediately reset the state without searching.
     * - Starts a loading state before performing the API request.
     * - Updates results on success.
     * - Sets a user-visible [message] when an error occurs.
     *
     * @param query The search keyword entered by the user.
     */
    fun searchUsers(query: String){
        if(query.isBlank() || query.length < 2 ){
            clearSearch()
            return
        }

        _uiState.value = SearchUiState(
            results = emptyList(),
            isLoading = true,
            message = null
        )

        // start search
        viewModelScope.launch {
            try{
                val results = apiService.searchUsers(query)
                _uiState.value = SearchUiState(
                    results = results,
                    isLoading = false,
                    message = null
                )

            } catch (ex: HttpException){
                _uiState.update {
                    it.copy(
                        results = emptyList(),
                        isLoading = false,
                        message = ErrorParser.errorHttpError(ex)
                    )
                }
            } catch (ex: IOException){
                _uiState.update {
                    it.copy(
                        results = emptyList(),
                        isLoading = false,
                        message = "Network error. Please check your connection."
                    )
                }
            }catch (ex: Exception) {
                _uiState.value = _uiState.value.copy(
                    results = emptyList(),
                    isLoading = false,
                    message = "Network error"
                )
            }
        }
    }
    /**
     * Resets the search state to its default values.
     */
    fun clearSearch(){
        _uiState.update {
            SearchUiState()
        }
    }
}