package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import com.example.active_portfolio_mobile.model.Adventure
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.model.AdventureUpdateRequest
import com.example.active_portfolio_mobile.network.ActivePortfolioApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

class AdventureCreationUpdateVM : ViewModel() {
    private var _message = mutableStateOf<String?>(null)
    val message: State<String?>
        get() = _message

    private var _adventure = MutableStateFlow(Adventure("", "", "", "", emptyList()))
    val adventure: StateFlow<Adventure>
        get() = _adventure.asStateFlow()

    fun setAdventure(adventure: Adventure) {
        _adventure = MutableStateFlow(adventure)
    }

    fun setUserId(id: String) {
        viewModelScope.launch {
            _adventure.update { it.copy(userId = id) }
        }
    }
    fun setTitle(title: String) {
        viewModelScope.launch {
            _adventure.update { it.copy(title = title) }
        }
    }
    fun setVisibility(visibility: String) {
        viewModelScope.launch {
            _adventure.update { it.copy(visibility = visibility) }
        }
    }
    fun addToPortfolios(portfolio: String) {
        viewModelScope.launch {
            _adventure.update { it.copy(portfolios = it.portfolios + portfolio) }
        }
    }
    fun removeFromPortfolios(portfolioToRemove: String) {
        viewModelScope.launch {
            _adventure.update {
                it.copy(
                    portfolios = it.portfolios.filterNot { portfolio -> portfolio == portfolioToRemove }
                )
            }
        }
    }

    fun saveAdventure() {
        viewModelScope.launch {
            try {
                var response: Response<Adventure>
                if (adventure.value.id == "") {
                    response = ActivePortfolioApi.adventure.create(adventure.value)
                } else {
                    val updatedAdventure = AdventureUpdateRequest(
                        newTitle = adventure.value.title,
                        newVisibility = adventure.value.visibility,
                        newPortfolios = adventure.value.portfolios
                    )
                    response = ActivePortfolioApi.adventure.update(
                        id = adventure.value.id,
                        updatedAdventure = updatedAdventure
                    )
                }
                if (response.isSuccessful) {
                    if (adventure.value.id == "") {
                        _adventure.update { it.copy(id = response.body()!!.id) }
                    }
                    _message.value = "Success"
                } else {
                    _message.value = response.message()
                }

            } catch(err: Exception) {
                println("An error occurred while creating/updating the Adventure: $err")
            }
        }
    }
}