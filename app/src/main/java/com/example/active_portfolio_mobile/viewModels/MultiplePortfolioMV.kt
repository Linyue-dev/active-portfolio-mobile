package com.example.active_portfolio_mobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
 * One VM per page so list and single for each .
 */
class PortfoliosVM : ViewModel(){
    private val _portfolios = MutableStateFlow<List<Portfolio>>(emptyList())
    val portfolios: StateFlow<List<Portfolio>> = _portfolios.asStateFlow()

    private val _portfolio = MutableStateFlow<Portfolio?>(null)
    val portfolio: StateFlow<Portfolio?> = _portfolio.asStateFlow()

    var portfolioState by mutableStateOf<Portfolio?>(null)
        private set
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAllPortfolio() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.getAll()
                }

                if (response.isSuccessful) {
                    _portfolios.value = response.body() ?: emptyList()
                    
                } else {
                    _errorMessage.value = "Error ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadOnePortfolio(portfolioId : String){
        viewModelScope.launch { 
            _isLoading.value = true
            _errorMessage.value = null
            try{
                val response = withContext(Dispatchers.IO){
                    ActivePortfolioApi.portfolio.getPortfolio(portfolioId)
                }

                if(response.isSuccessful){
                    _portfolio.value = response.body()
                    portfolioState = response.body() 
                }else{
                    _errorMessage.value = "Error ${response.code()}"
                } 
            }catch(e: Exception){
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}