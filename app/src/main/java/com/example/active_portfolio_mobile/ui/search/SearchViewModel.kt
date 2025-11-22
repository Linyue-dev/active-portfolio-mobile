package com.example.active_portfolio_mobile.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.User
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
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
    private val apiService = ActivePortfolioApi.userPublic
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun searchUsers(query: String){
        Log.d("SearchResultsPage", "Query received: $query")
        if(query.isBlank()){
            Log.d("SearchViewModel", "Query is blank, clearing")
            _uiState.value = SearchUiState(
                results = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }

        if(query.length <2 ){
            Log.d("SearchViewModel", "Query too short: ${query.length}")
            _uiState.value = SearchUiState(
                results = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }
        Log.d("SearchViewModel", "Starting search for: '$query'")
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
                Log.e("SearchViewModel", "EXCEPTION in searchUsers", ex)  // ← 加这个
                Log.e("SearchViewModel", "Exception type: ${ex.javaClass.name}")
                Log.e("SearchViewModel", "Exception message: ${ex.message}")
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