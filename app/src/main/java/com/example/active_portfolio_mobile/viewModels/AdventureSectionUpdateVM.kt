package com.example.active_portfolio_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionUpdateRequest
import com.example.active_portfolio_mobile.data.remote.dto.AdventureUpdateRequest
import com.example.active_portfolio_mobile.network.ActivePortfolioApi
import com.example.active_portfolio_mobile.network.ActivePortfolioApi.adventure
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdventureSectionUpdateVM : ViewModel() {
    private var _message = mutableStateOf<String?>(null)
    val message: State<String?>
        get() = _message

//    private var _adventureId = mutableStateOf("")
//    val adventureId: State<String>
//        get() = _adventureId
//    fun setAdventureId(adventureId: String) {
//        _adventureId = mutableStateOf(adventureId)
//    }

    var sections = mutableStateOf<List<AdventureSection>>(emptyList())
        private set
    fun fetchSections(adventureId: String) {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventureSection.getSectionsOfAdventure(
                    adventureId = adventureId
                )
                if (response.isSuccessful) {
                    sections.value = response.body() ?: emptyList()
                } else {
                    _message.value = response.message()
                }
            } catch(e: Exception) {
                println(e)
            }
        }
    }

    /**
     * Saves updates to an existing section in the database via the ActivePortfolio API.
     * @param section the updated Adventure Section to be saved.
     */
    fun updateSection(section: AdventureSection, setMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val updatedSection = AdventureSectionUpdateRequest(
                    newLabel = section.label,
                    newContentString = section.content,
                    newDescription = section.description ?: "",
                    newPortfolios = section.portfolios
                )
                val response = ActivePortfolioApi.adventureSection.updateSection(
                    sectionId = section.id,
                    updatedSection = updatedSection
                )
                if (response.isSuccessful) {
                    setMessage("Success")
                } else {
                    setMessage(response.message())
                }
            } catch(err: Exception) {
                println("An error occurred while creating/updating the Adventure Section: $err")
            }
        }
    }

    /**
     * Deletes an adventure section through the Active Portfolio API.
     * @param section the adventure section to be deleted.
     */
    fun deleteSection(section: AdventureSection, setMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventureSection.delete(section.id)
                if (response.isSuccessful) {
                   setMessage("Success")
                    sections.value = sections.value.filterNot { it.id == section.id }
                } else {
                    setMessage(response.message())
                }
            } catch(err: Exception) {
                println("An error occurred while trying to delete the Adventure: $err")
            }
        }
    }
}