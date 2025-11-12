package com.example.active_portfolio_mobile.viewModels

import android.R.attr.description
import android.R.attr.label
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSectionUpdateRequest
import com.example.active_portfolio_mobile.data.remote.dto.AdventureUpdateRequest
import com.example.active_portfolio_mobile.data.remote.network.ActivePortfolioApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import kotlin.collections.mapIndexed

class AdventureSectionUpdateVM : ViewModel() {
    private var _message = mutableStateOf<String?>(null)
    val message: State<String?>
        get() = _message

    var sections = mutableStateOf<List<AdventureSection>>(emptyList())
        private set
    fun fetchSections(adventureId: String, filterPortfolioId: String = "") {
        viewModelScope.launch {
            try {
                val response = ActivePortfolioApi.adventureSection.getSectionsOfAdventure(
                    adventureId = adventureId,
                    filterPortfolio = filterPortfolioId
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
                println("An error occurred while trying to delete the Section: $err")
            }
        }
    }
}

class AdventureSectionImageUpdateVM : ViewModel() {

    private var _section = MutableStateFlow(AdventureSection("", "", "", "", emptyList(), "", ""))
    val section: StateFlow<AdventureSection>
        get() = _section.asStateFlow()

    fun setSection(section: AdventureSection) {
        viewModelScope.launch {
            try {
                _section.update { section }
                decodeBase64Images()
            } catch(e: Exception) {
                println(e)
            }
        }
    }

    private var _bitmapImages = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmapImages: StateFlow<List<Bitmap>>
        get() = _bitmapImages.asStateFlow()

    /**
     * Convert the base64 strings sent from the API into bitmaps which are viewable in the Android
     * Image viewer.
     * This code was provided by MS Copilot.
     */
    fun decodeBase64Images() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val jsonArray = JSONArray(_section.value.content)
                val bitmaps = mutableListOf<Bitmap>()

                for (i in 0 until jsonArray.length()) {
                    val base64 = jsonArray.optString(i)
                    if (base64.isNotBlank()) {
                        val rawBase64 = base64.substringAfter("base64,", base64)
                        val bytes = Base64.decode(rawBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        bitmap?.let { bitmaps.add(it) }
                    }
                }

                withContext(Dispatchers.Main) {
                    _bitmapImages.value = bitmaps
                }
            } catch (e: JSONException) {
                Log.e("ViewModel", "Failed to parse and convert base64 images", e)
            }
        }
    }

    fun addImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _bitmapImages.update { it + bitmap }
        }
    }
    fun removeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _bitmapImages.update { it.filterNot { image -> image === bitmap } }
        }
    }

    fun setLabel(label: String) {
        viewModelScope.launch {
            _section.update { it.copy(label = label) }
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

    /**
     * Saves updates to an existing image section in the database via the ActivePortfolio API.
     * @param section the updated Adventure Section to be saved.
     */
    fun updateSection(setMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val convertedImages = convertImagesForRequest(
                    id = _section.value.id,
                    images = _bitmapImages.value
                )
                val response = ActivePortfolioApi.adventureSection.updateImageSection(
                    id = _section.value.id,
                    label = _section.value.label.toRequestBody("text/plain".toMediaType()),
                    description = (_section.value.description ?: "").toRequestBody("text/plain".toMediaType()),
                    newContentFiles = convertedImages
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
}


fun convertImagesForRequest(
    id: String,
    images: List<Bitmap>,
): List<MultipartBody.Part> {
    // Determine the name of the files array in the request by whether this is a new entry or
    // updating an existing one.
    val imageParameterName = if (id == "") "contentFiles" else "newContentFiles"

    // Translate all image bitmaps into byte arrays so that they can be processed by the backend API.
    // This portion of the code was built with the assistance of MS Copilot.
    return images.mapIndexed { index, bitmap ->
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
        val byteArray = byteStream.toByteArray()

        val requestBody = byteArray.toRequestBody("image/png".toMediaType())
        MultipartBody.Part.createFormData(
            name = imageParameterName,
            filename = "image_$index.png",
            body = requestBody
        )
    }
}