package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdventureSectionCreationVM : ViewModel() {
    private var _section = MutableStateFlow(AdventureSection("", "", "", "", emptyList(), "", ""))
    val section: StateFlow<AdventureSection>
        get() = _section.asStateFlow()

    fun setAdventureId(adventureId: String) {
        viewModelScope.launch {
            _section.update { it.copy(adventureId = adventureId) }
        }
    }

    fun setLabel(label: String) {
        viewModelScope.launch {
            _section.update { it.copy(label = label) }
        }
    }

    fun setType(type: String) {
        viewModelScope.launch {
            _section.update { it.copy(type = type) }
        }
    }

    fun setDescription(description: String) {
        viewModelScope.launch {
            _section.update { it.copy(description = description) }
        }
    }

    /**
     * Add a portfolio to the list of portfolios in which the section is included.
     * @param portfolio The ID of the portfolio to add (24 hex characters).
     */
    fun addToPortfolios(portfolio: String) {
        viewModelScope.launch {
            _section.update { it.copy(portfolios = it.portfolios + portfolio) }
        }
    }
    /**
     * Remove a portfolio from the list of portfolios in which the section is included.
     * @param portfolioToRemove The ID of the portfolio to remove (24 hex characters).
     */
    fun removeFromPortfolios(portfolioToRemove: String) {
        viewModelScope.launch {
            _section.update {
                it.copy(
                    portfolios = it.portfolios.filterNot { portfolio -> portfolio == portfolioToRemove }
                )
            }
        }
    }


}