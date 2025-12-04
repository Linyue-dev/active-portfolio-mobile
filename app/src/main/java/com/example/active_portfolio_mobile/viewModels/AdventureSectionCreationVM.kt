package com.example.active_portfolio_mobile.viewModels

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionCreationRequest
import com.example.active_portfolio_mobile.data.remote.dto.Portfolio
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class AdventureSectionCreationVM : ViewModel() {
    private var _message = MutableStateFlow("")
    fun getMessage(): String {
        return _message.value
    }

    private var _section = MutableStateFlow(AdventureSection("", "", "", "", emptyList(), "", ""))
    val section: StateFlow<AdventureSection>
        get() = _section.asStateFlow()

    var portfolios = mutableStateOf<List<Portfolio>>(emptyList())
        private set
    fun fetchPortfolios(adventureId: String) {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.portfolio.getPortfoliosByAdventure(
                    adventureId
                )
                if (response.isSuccessful) {
                    portfolios.value = response.body() ?: emptyList()
                } else {
                    println(response.errorBody())
                    _message.value = response.errorBody().toString()
                }
            } catch (e: Exception) {
                println(e)
                _message.value = e.message ?: "Unknown error fetching portfolios"
            }
        }
    }

    fun setAdventureId(adventureId: String) {
        viewModelScope.launch {
            _section.update { it.copy(adventureId = adventureId) }
        }
    }

    /**
     * The function for saving an adventure section to the database which has a content type which
     * takes a simple string.
     */
    fun saveNewSection(sectionToSave: AdventureSection, token: String?, setSuccess: (Boolean) -> Unit) {
        viewModelScope.launch{
            try {
                val request = AdventureSectionCreationRequest(
                    label = sectionToSave.label,
                    contentString = sectionToSave.content,
                    description = sectionToSave.description ?: "",
                    portfolios = sectionToSave.portfolios,
                    adventureId = section.value.adventureId,
                    type = sectionToSave.type
                )
                val response = ActivePortfolioApi.adventureSection.createSection(
                    "Bearer $token",
                    request
                )
                if (response.isSuccessful) {
                    setSuccess(true)
                } else {
                    println(response.message())
                    _message.value = response.message()
                    setSuccess(false)
                }

            } catch (e: Error) {
                println(e)
                _message.value = e.message ?: "Unknown error saving new section."
                setSuccess(false)
            }
        }
    }

    /**
     * The function for saving an image adventure section to the database.
     */
    fun saveNewImageSection(
        sectionToSave: AdventureSection,
        images: List<Bitmap>,
        token: String?,
        setSuccess: (Boolean) -> Unit
    ) {
        viewModelScope.launch{
            try {
                // convert the images to byte array
                val convertedImages = convertImagesForRequest(
                    id = _section.value.id,
                    images = images
                )
                val response = ActivePortfolioApi.adventureSection.createImageSection(
                    token = "Bearer $token",
                    label = sectionToSave.label.toRequestBody("text/plain".toMediaType()),
                    description = (sectionToSave.description ?: "").toRequestBody("text/plain".toMediaType()),
                    type = sectionToSave.type.toRequestBody("text/plain".toMediaType()),
                    adventureId = section.value.adventureId.toRequestBody("text/plain".toMediaType()),
                    contentFiles = convertedImages
                )
                if (response.isSuccessful) {
                    setSuccess(true)
                } else {
                    println(response.message())
                    _message.value = response.message()
                    setSuccess(false)
                }

            } catch (e: Error) {
                println(e)
                _message.value = e.message ?: "Unknown error while attempting to save section"
                setSuccess(false)
            }
        }
    }
}