package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PortfolioMV: ViewModel(){
    private val _adventures =  MutableStateFlow<List<Adventure>>(emptyList())
    val adventures: StateFlow<List<Adventure>> = _adventures.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadAdventureFromPortfolio(portfolioId : String){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try{
                val response = withContext(Dispatchers.IO){
                    ActivePortfolioApi.adventure.getAllAdventureFromPortfolio(portfolioId)
                }
                if(response.isSuccessful){
                    _adventures.value = response.body() ?: emptyList()
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