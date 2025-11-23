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
data class SearchUiState(
    val results: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
class SearchViewModel(
): ViewModel(){
    private val apiService = RetrofitClient.createPublicService(UserPublicApiService::class.java)
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun searchUsers(query: String){

        if(query.isBlank()){
            _uiState.value = SearchUiState(
                results = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }

        if(query.length <2 ){
            _uiState.value = SearchUiState(
                results = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        // start search
        viewModelScope.launch {
            try{
                val results = apiService.searchUsers(query)
                _uiState.value = SearchUiState(
                    results = results,
                    isLoading = false,
                    error = null
                )

            } catch (ex: HttpException){
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorParser.errorHttpError(ex)
                    )
                }
            } catch (ex: IOException){
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Network error. Please check your connection."
                    )
                }
            }catch (ex: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error"
                )
            }
        }
    }

    fun clearSearch(){
        _uiState.update {
            SearchUiState()
        }
    }
}