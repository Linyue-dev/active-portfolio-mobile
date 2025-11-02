package com.example.active_portfolio_mobile.viewModels

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureUpdateRequest
import com.example.active_portfolio_mobile.network.ActivePortfolioApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * A view model which handles operations related to an Adventure object.
 */
class AdventureCreationUpdateVM : ViewModel() {
    private var _message = mutableStateOf<String?>(null)
    val message: State<String?>
        get() = _message

    private var _adventure = MutableStateFlow(Adventure("", "", "", "", emptyList()))
    val adventure: StateFlow<Adventure>
        get() = _adventure.asStateFlow()

    fun setAdventure(id: String) {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventure.getOne(id)
                if (response.isSuccessful) {
                    _adventure.update { response.body()!! }
                } else {
                  _message.value = "There was an issue retrieving the adventure."
                }
            } catch(e: Exception) {
                println(e)
            }
        }
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

    /**
     * Add a portfolio to the list of portfolios in which the adventure is included.
     * @param portfolio The ID of the portfolio to add (24 hex characters).
     */
    fun addToPortfolios(portfolio: String) {
        viewModelScope.launch {
            _adventure.update { it.copy(portfolios = it.portfolios + portfolio) }
        }
    }
    /**
     * Remove a portfolio from the list of portfolios in which the adventure is included.
     * @param portfolioToRemove The ID of the portfolio to remove (24 hex characters).
     */
    fun removeFromPortfolios(portfolioToRemove: String) {
        viewModelScope.launch {
            _adventure.update {
                it.copy(
                    portfolios = it.portfolios.filterNot { portfolio -> portfolio == portfolioToRemove }
                )
            }
        }
    }

    /**
     * Creates an Adventure or updates an existing one depending on whether the view model's
     * adventure has a set id. Sets the adventure id once a new Adventure is created so it can
     * proceed to be updated when this function is called again.
     */
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

    /**
     * Deletes an adventure through the Active Portfolio API using the current id value of view model's
     * adventure. The values for the view model's held adventure are then reset (except for userId),
     * allowing a new adventure to be defined and created.
     */
    fun deleteAdventure() {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventure.delete(adventure.value.id)
                if (response.isSuccessful) {
                    _adventure.update { it.copy(id = "", title = "", visibility = "", portfolios = emptyList()) }
                    _message.value = "Success"
                } else {
                    _message.value = response.message()
                }
            } catch(err: Exception) {
                println("An error occurred while trying to delete the Adventure: $err")
            }
        }
    }
}