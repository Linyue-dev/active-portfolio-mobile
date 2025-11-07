package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.network.ActivePortfolioApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdventureVM : ViewModel() {
    private val _adventures = MutableStateFlow<List<Adventure>>(emptyList())
    val adventures: StateFlow<List<Adventure>> = _adventures.asStateFlow()

    fun fetchAllAdventures() {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventure.getAll()
                if (response.isSuccessful) {
                    _adventures.value = response.body() ?: emptyList()
                }
            } catch(err: Exception) {
                println("An error occurred when fetching all Adventures: $err")
            }
        }
    }

    fun fetchAdventuresByUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventure.getAllByUser(userId)
                if (response.isSuccessful) {
                    _adventures.value = response.body() ?: emptyList()
                } else {
                    println(response.errorBody().toString())
                }
            } catch(err: Exception) {
                println("An error occurred when fetching all Adventures by user: $err")
            }
        }
    }
}