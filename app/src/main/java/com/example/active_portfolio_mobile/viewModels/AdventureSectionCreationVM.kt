package com.example.active_portfolio_mobile.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionCreationRequest
import com.example.active_portfolio_mobile.network.ActivePortfolioApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class AdventureSectionCreationVM : ViewModel() {
    private var _section = MutableStateFlow(AdventureSection("", "", "", "", emptyList(), "", ""))
    val section: StateFlow<AdventureSection>
        get() = _section.asStateFlow()

    fun setAdventureId(adventureId: String) {
        viewModelScope.launch {
            _section.update { it.copy(adventureId = adventureId) }
        }
    }

    /**
     * The function for saving an adventure section to the database which has a content type which
     * takes a simple string.
     */
    fun saveNewSection(sectionToSave: AdventureSection, setSuccess: (Boolean) -> Unit) {
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
                    request
                )
                if (response.isSuccessful) {
                    setSuccess(true)
                } else {
                    println(response.message())
                    setSuccess(false)
                }

            } catch (e: Error) {
                println(e)
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
                    label = sectionToSave.label.toRequestBody("text/plain".toMediaType()),
                    description = sectionToSave.label.toRequestBody("text/plain".toMediaType()),
                    type = sectionToSave.type.toRequestBody("text/plain".toMediaType()),
                    adventureId = section.value.adventureId.toRequestBody("text/plain".toMediaType()),
                    contentFiles = convertedImages
                )
                if (response.isSuccessful) {
                    setSuccess(true)
                } else {
                    println(response.message())
                    setSuccess(false)
                }

            } catch (e: Error) {
                println(e)
                setSuccess(false)
            }
        }
    }
}