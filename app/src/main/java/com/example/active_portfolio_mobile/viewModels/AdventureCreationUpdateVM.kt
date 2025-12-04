package com.example.active_portfolio_mobile.viewModels

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureUpdateRequest
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
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
                _message.value = e.message ?: "An unknown error occurred while trying to get the adventure."
                println(e)
            }
        }
    }

    var portfolios = mutableStateOf<List<Portfolio>>(emptyList())
        private set

    fun fetchPortfolios() {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.portfolio.getAllUsersPortfolio(
                    _adventure.value.userId
                )
                if (response.isSuccessful) {
                    portfolios.value = response.body() ?: emptyList()
                } else {
                    println(response.errorBody())
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun setUserId(id: String) {
        viewModelScope.launch {
            _adventure.update { it.copy(userId = id) }
            fetchPortfolios()
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
     * @param token the token for authorizing the user creating the adventure. If the token
     * is null or invalid, there will be an error.
     */
    fun saveAdventure(token: String?, setMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                var response: Response<Adventure>
                if (adventure.value.id == "") {
                    response = ActivePortfolioApi.adventure.create(
                        "Bearer $token",
                        adventure.value
                    )
                } else {
                    val updatedAdventure = AdventureUpdateRequest(
                        newTitle = adventure.value.title,
                        newVisibility = adventure.value.visibility,
                        newPortfolios = adventure.value.portfolios
                    )
                    response = ActivePortfolioApi.adventure.update(
                        id = adventure.value.id,
                        token = "Bearer $token",
                        updatedAdventure = updatedAdventure
                    )
                }
                if (response.isSuccessful) {
                    if (adventure.value.id == "") {
                        _adventure.update { it.copy(id = response.body()!!.id) }
                    }
                    setMessage("Success")
                } else {
                    setMessage(response.message())
                }
            } catch(err: Exception) {
                println("An error occurred while creating/updating the Adventure: $err")
                setMessage("An error occurred while creating/updating the Adventure: $err")
            }
        }
    }

    /**
     * Deletes an adventure through the Active Portfolio API using the current id value of view model's
     * adventure. The values for the view model's held adventure are then reset (except for userId),
     * allowing a new adventure to be defined and created.
     * @param token the token for authorizing the user deleting the adventure. If the token
     * is null or invalid, there will be an error.
     */
    fun deleteAdventure(token: String?, setMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventure.delete(
                    "Bearer $token",
                    adventure.value.id
                )
                if (response.isSuccessful) {
                    _adventure.update { it.copy(id = "", title = "", visibility = "", portfolios = emptyList()) }
                    setMessage("Success")
                } else {
                    setMessage(response.message())
                }
            } catch(err: Exception) {
                println("An error occurred while trying to delete the Adventure: $err")
                setMessage("An error occurred while trying to delete the Adventure: $err")
            }
        }
    }
}