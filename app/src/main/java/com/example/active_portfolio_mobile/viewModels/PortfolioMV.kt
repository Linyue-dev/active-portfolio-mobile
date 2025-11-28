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

/**
 * Viewmodel responsible for loading and exposing all adventures
 * linked to a specific portfolio.
 * It handles adventure list state, loading state and error message state.
 */
class PortfolioMV: ViewModel(){
    //Holds the list of adventures returned by the viewmodel.
    private val _adventures =  MutableStateFlow<List<Adventure>>(emptyList())
    val adventures: StateFlow<List<Adventure>> = _adventures.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    //Loads all adventures belonging to a given portfolio.
    //Makes a network request on a background thread,
    //updates state flows based on success or failure.
    fun loadAdventureFromPortfolio(portfolioId : String){
        viewModelScope.launch {
            //Start loading and clear previous errors.
            _isLoading.value = true
            _errorMessage.value = null
            
            try{
                
                //Call the adventures
                val response = withContext(Dispatchers.IO){
                    ActivePortfolioApi.adventure.getAllAdventureFromPortfolio(portfolioId)
                }

                //Handle success or failure cases
                if(response.isSuccessful){
                    _adventures.value = response.body() ?: emptyList()
                }else{
                    _errorMessage.value = "Error ${response.code()}"
                }
            }catch(e: Exception){
                //Any unexpected failure
                _errorMessage.value = e.message
            } finally {
                //Stop loading animation regardless of result
                _isLoading.value = false
            }
        }
    }
}