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
 * Viewmodel responsible for retrieving portfolios from the backend.
 * It responsible to load all portfolio, load one portfolio using portfolioID
 * Provide Ui with loading status, error messages, and portfolio list and selected portfolio.
 */
class GetPortfoliosVM : ViewModel(){
    //Holds list of all portfolios loaded from the backend
    private val _portfolios = MutableStateFlow<List<Portfolio>>(emptyList())
    val portfolios: StateFlow<List<Portfolio>> = _portfolios.asStateFlow()

    //Hold the currently selected /loaded portfolio
    private val _portfolio = MutableStateFlow<Portfolio?>(null)
    val portfolio: StateFlow<Portfolio?> = _portfolio.asStateFlow()

    var portfolioState by mutableStateOf<Portfolio?>(null)
        private set

    //Loading indication for the backend request
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //Holds error message for the UI
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Load all portfolios from the backend
     * Trigger loading state, handles response,
     * updates portfolio list or error messages.
     */
    fun loadAllPortfolio() {
        viewModelScope.launch {
            
            //Start loading and reset previous error
            _isLoading.value = true
            _errorMessage.value = null
            try {
                //Perform network call on IO dispatcher
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.getAll()
                }

                //Check the response of the call
                if (response.isSuccessful) {
                    _portfolios.value = response.body() ?: emptyList()
                    
                } else {
                    _errorMessage.value = "Error ${response.code()}"
                }
            } catch (e: Exception) {
                //Catch unexpected failure
                _errorMessage.value = e.message
            } finally {
                //Stop loading indicator regardless of result
                _isLoading.value = false
            }
        }
    }

    /**
     * Loads a single portfolio by its ID.
     */
    fun loadOnePortfolio(portfolioId : String){
        viewModelScope.launch { 
            
            //Begin loading state
            _isLoading.value = true
            _errorMessage.value = null
            try{
                //Request
                val response = withContext(Dispatchers.IO){
                    ActivePortfolioApi.portfolio.getPortfolio(portfolioId)
                }

                //Handle successful response
                if(response.isSuccessful){
                    _portfolio.value = response.body()
                    portfolioState = response.body() 
                }else{
                    _errorMessage.value = "Error ${response.code()}"
                } 
            }catch(e: Exception){
                //Process error
                _errorMessage.value = e.message
            } finally {
                //End loading.
                _isLoading.value = false
            }
        }
    }
}