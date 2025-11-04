package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.CreatePortfolioRequest
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.dto.UpdatePortfolioRequest
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class SinglePortfolioMV : ViewModel() {

    // Holds the newly created portfolio
    private val _portfolio = MutableStateFlow<Portfolio?>(null)
    val portfolio: StateFlow<Portfolio?> = _portfolio.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _connectionStatus = MutableStateFlow<String?>(null)
    val connectionStatus: StateFlow<String?> = _connectionStatus.asStateFlow()
    /**
     * Creates a new portfolio by calling the backend.
     */
    fun createPortfolio(portfolio: CreatePortfolioRequest) {
        viewModelScope.launch {
            runSafeOperation {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.createPortfolio(portfolio)
                }

                if (response.isSuccessful) {
                    _portfolio.value = response.body()
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            }
        }
    }

    /**
     * Update an existing portfolio by ID.
     */
    fun updatePortfolio(portfolioId: String, updated: UpdatePortfolioRequest){
        viewModelScope.launch {
            runSafeOperation {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.updatePortfolio(portfolioId, updated)
                }

                if (response.isSuccessful) {
                    _portfolio.value = response.body()
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            }
        }
    }

    /**
     * Deletes a portfolio by ID
     */
    fun deletePortfolio(portfolioId: String){
        viewModelScope.launch{
            runSafeOperation {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.deletePortfolio(portfolioId)
                }

                if (response.isSuccessful && response.body() == true) {
                    _portfolio.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            }
        }
    }

    fun resetState() {
        _portfolio.value = null
        _errorMessage.value = null
        _isLoading.value = false
    }

    private suspend fun runSafeOperation(block: suspend() -> Unit){
        _isLoading.value = true
        _errorMessage.value = null

        try{
            block()
        } catch(e: Exception){
            _errorMessage.value = e.message
        } finally{
            _isLoading.value = false
        }
    }

    fun testBackendConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("https://activeportfolio.onrender.com/portfolio/allPortfolio")
                val conn = url.openConnection() as HttpURLConnection
                conn.connect()
                val code = conn.responseCode

                withContext(Dispatchers.Main) {
                    _connectionStatus.value = if (code == 200) {
                        "✅ Connected successfully! (HTTP $code)"
                    } else {
                        "⚠️ Server responded with HTTP $code"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _connectionStatus.value = "❌ Connection failed: ${e.message}"
                }
            }
        }
    }

}