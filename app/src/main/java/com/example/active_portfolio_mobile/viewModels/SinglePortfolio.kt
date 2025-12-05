package com.example.active_portfolio_mobile.viewModels

import android.util.Log
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
import org.json.JSONObject

/**
 * View model responsible for managing a single portfolio and all
 * create/update/delete operations related to it.
 * This viewmodel is designed for screens that create or modify a single portfolio
 * at a time.
 */
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

    //Error code returned
    private val _errorCode = MutableStateFlow<Int?>(null)
    val errorCode: StateFlow<Int?> = _errorCode.asStateFlow()

    //Pop up message
    private val _popUpMessage = MutableStateFlow<String?>(null)
    val popUpMessage : StateFlow<String?>  = _popUpMessage.asStateFlow()
    
    private val _connectionStatus = MutableStateFlow<String?>(null)
    val connectionStatus: StateFlow<String?> = _connectionStatus.asStateFlow()
    /**
     * Creates a new portfolio by calling the backend.
     */
    fun createPortfolio(token : String?, portfolio: CreatePortfolioRequest) {
        viewModelScope.launch {
            runSafeOperation {
                
                //Get the response from the action
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.createPortfolio("Bearer $token",portfolio)
                }

                if (response.isSuccessful) {
                    _portfolio.value = response.body()
                    _popUpMessage.value = "SUCCESS: Portfolio created successfully!"
                } else {
                    val serverMessage = response.errorBody()?.string()?.let{
                        body->
                        try{
                            //Get the specific error message the back end send.
                            JSONObject(body).optString("errorMessage", "")
                        }
                        catch(e: Exception){
                            ""
                        }
                    }
                    _errorMessage.value = serverMessage?.ifBlank {
                        "Error ${response.code()}: ${response.message()}"
                    }
                    _errorCode.value = response.code()
                    _popUpMessage.value = "Failed to create portfolio."
                }
            }
        }
    }

    /**
     * Update an existing portfolio by ID.
     */
    fun updatePortfolio(token: String?, portfolioId: String, updated: UpdatePortfolioRequest){
        viewModelScope.launch {
            runSafeOperation {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.updatePortfolio("Bearer $token",portfolioId, updated)
                }
                Log.d("UPDATE_RESPONSE", "Code = ${response.code()} Body = ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    _portfolio.value = response.body()
                    _popUpMessage.value = "SUCCESS: Portfolio updated successfully!"
                } else {
                    val serverMessage = response.errorBody()?.string()?.let{
                            body->
                        try{
                            //Get the specific error message the back end send.
                            JSONObject(body).optString("errorMessage", "")
                        }
                        catch(e: Exception){
                            ""
                        }
                    }
                    _errorCode.value = response.code()
                    _errorMessage.value = serverMessage?.ifBlank {
                        "Error ${response.code()}: ${response.message()}"
                    }
                    _popUpMessage.value = "Failed to update portfolio."
                }
            }
        }
    }

    /**
     * Deletes a portfolio by ID
     */
    fun deletePortfolio(token: String?, portfolioId: String){
        viewModelScope.launch{
            runSafeOperation {
                val response = withContext(Dispatchers.IO) {
                    ActivePortfolioApi.portfolio.deletePortfolio("Bearer $token",portfolioId)
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    _portfolio.value = null
                    _popUpMessage.value = "SUCCESS: Portfolio deleted successfully."
                } else {
                    val serverMessage = response.errorBody()?.string()?.let{
                            body->
                        try{
                            //Get the specific error message the back end send.
                            JSONObject(body).optString("errorMessage", "")
                        }
                        catch(e: Exception){
                            ""
                        }
                    }
                    _errorCode.value = response.code()
                    _errorMessage.value = serverMessage?.ifBlank {
                        "Error ${response.code()}: ${response.message()}"
                    }
                    _popUpMessage.value = "Failed to delete portfolio."
                }
            }
        }
    }

    //Helper to reset message after showing
    fun ClearPopUpMessage(){
        _popUpMessage.value = null
    }

    //Helper function - Clean the state and then try the operation
    private suspend fun runSafeOperation(block: suspend() -> Unit){
        _isLoading.value = true
        _errorMessage.value = null
        _errorCode.value = null

        try{
            block()
        } catch(e: Exception){
            _errorMessage.value = e.message
        } finally{
            _isLoading.value = false
        }
    }

}