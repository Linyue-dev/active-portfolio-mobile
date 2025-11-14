package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * View model that manages fetching and storing user profile data
 */
class UserPortfolio: ViewModel(){
    // Holds the user portfolios
    private val _portfolios = MutableStateFlow<List<Portfolio?>>(emptyList())
    val portfolios: StateFlow<List<Portfolio?>> = _portfolios.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Fetches all portfolio created by a specific user,
     *
     * @param id The user ID to fetch portfolio
     */
    fun allUserPortfolio(id: String){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.getAllUsersPortfolio(id)
                }

                if (response.isSuccessful) {
                    _portfolios.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            }
            catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fetches only the user portfolio of a specific visibility level.
     */
    fun allUserVisibilityPortfolio(id: String, visibility: String){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.getAllPortfolioByVisibility(visibility,id)
                }

                if (response.isSuccessful) {
                    _portfolios.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            }
            catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Suspend and return the number of total portfolios for a user.
     *
     * @param userId The ID of the user
     */
    suspend fun getPortfolioCount(userId: String): Int{
        return withContext(Dispatchers.IO) {
            try {
                val response = ActivePortfolioApi.portfolio.getAllUsersPortfolio(userId)
                if (response.isSuccessful) {
                    response.body()?.size ?: 0
                } else 0
            } catch (e: Exception) {
                0
            }
        }
    }
    /**
     * Suspend and return the number of portfolios for a user about a certain
     * potter.
     *
     * @param userId The ID of the user
     * @param visibility The visibility of the portfolios to retrieves
     */
    suspend fun getPortfolioCountByVisibility(userId: String, visibility: String): Int{
        return withContext(Dispatchers.IO) {
            try {
                val response = ActivePortfolioApi.portfolio.getAllPortfolioByVisibility(visibility, userId)
                if (response.isSuccessful) {
                    response.body()?.size ?: 0
                } else 0
            } catch (e: Exception) {
                0
            }
        }
    }
}