package com.example.active_portfolio_mobile.composables.adventure

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.utilities.rememberMutableStateListOf
import com.example.active_portfolio_mobile.viewModels.AdventureSectionCreationVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.forEach

/**
 * A form for the creation of an Adventure Section.
 * @param type the type of the section being created with this form (text, image, link, etc.)
 * @param sectionVM the View Model for creating sections from the creation screen.
 */
@Composable
fun CreateSectionForm(
    type: String,
    messageFlow: MutableSharedFlow<String>,
    sectionVM: AdventureSectionCreationVM
) {
    val scope = rememberCoroutineScope()
    var label by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var stringContent by rememberSaveable { mutableStateOf("") }
    val imageContent = rememberMutableStateListOf<Bitmap>()
    val portfolios = rememberMutableStateListOf<String>()
    val navController: NavController = LocalNavController.current
    val parentPortfolios = sectionVM.portfolios
    val authViewModel = LocalAuthViewModel.current

    Column {
        // Create the Section's label.
        TextField(
            label = { Text("Label") },
            value = label,
            onValueChange = { label = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ), modifier = Modifier.fillMaxWidth()
        )

        // Create the section's content. Will depend on the type of section being created.
        when (type) {
            SectionType.TEXT -> CreateStringSectionContent(
                contentType = SectionType.TEXT,
                content = stringContent,
            ) { stringContent = it }

            SectionType.LINK -> CreateStringSectionContent(
                contentType = SectionType.LINK,
                content = stringContent,
            ) { stringContent = it }

            SectionType.IMAGE -> CreateImageSectionContent(
                bitmaps = imageContent,
                addImage = { imageContent.add(it) },
                removeImage = { imageContent.remove(it) }
            )
        }

        // Create the section's description (if the type allows it).
        if (type != SectionType.TEXT) {
            TextField(
                label = { Text("Description") },
                value = description,
                onValueChange = { description = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ), modifier = Modifier.fillMaxWidth()
            )
        }

        /**
         * Upon team reflection, we determined that setting portfolios for sections was excessive
         * and confusing. We have therefore decided to simply have all sections appear in a
         * portfolio that includes their parent adventure.
         */
//        // Set the portfolios in which to include this Section.
//        if (parentPortfolios.value.isNotEmpty()) {
//            DropDownTab(name = "Portfolios") {
//                MultiSelectList(
//                    selectedItems = parentPortfolios.value.filter{ it.id in portfolios },
//                    list = parentPortfolios.value,
//                    displayText = { it.title },
//                    selectItem = {
//                        portfolios.add(it.id)
//                    },
//                    deselectItem = {
//                        portfolios.remove(it.id)
//                    }
//                )
//            }
//        }

        // Save the created section.
        Button(onClick = {
            var success = false
            val sectionToSave = AdventureSection(
                label = label,
                type = type,
                description = description,
                portfolios = portfolios,
                content = stringContent,
                id = "",
                adventureId = ""
            )
            // Save the section according to its type.
            when (type) {
                SectionType.IMAGE -> sectionVM.saveNewImageSection(
                    token = authViewModel.tokenManager.getToken(),
                    sectionToSave = sectionToSave,
                    images = imageContent
                ){
                    success = it
                    if (success) {
                        scope.launch {
                            messageFlow.emit("Success")
                        }
                        navController.navigateUp()
                    } else {
                        scope.launch {
                            messageFlow.emit("Something went wrong: ${sectionVM.getMessage()}")
                        }
                    }
                }
                else -> sectionVM.saveNewSection(
                    token = authViewModel.tokenManager.getToken(),
                    sectionToSave = sectionToSave
                ) {
                    success = it
                    if (success) {
                        scope.launch {
                            messageFlow.emit("Success")
                        }
                        navController.navigateUp()
                    } else {
                        scope.launch {
                            messageFlow.emit("Something went wrong: ${sectionVM.getMessage()}")
                        }
                    }
                }
            }
        }) {
            Text("Save")
        }
    }
}

/**
 * A field for defining the section's content in section types whose content is a string.
 */
@Composable
fun CreateStringSectionContent(contentType: String, content: String, setSectionContent: (String) -> Unit) {
    TextField(
        label = { Text(contentType) },
        value = content,
        onValueChange = setSectionContent,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ), modifier = Modifier.fillMaxWidth()
    )
}

/**
 * A field for defining the content of an image section.
 * @param bitmaps the list of bitmap image files to which new images will be added.
 * @param addImage a function for adding an image to the list of bitmaps.
 * @param removeImage a function for removing an image from the list of bitmaps.
 */
@Composable
fun CreateImageSectionContent(
    bitmaps: List<Bitmap>,
    addImage: (Bitmap) -> Unit,
    removeImage: (Bitmap) -> Unit
) {
    Card {
        Column {
            bitmaps.forEach { bitmap ->
                Card {
                    Row {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.width(200.dp)
                        )
                        DeleteButtonWithConfirm {
                            removeImage(bitmap)
                        }
                    }
                }
            }
            ImagePicker {
                addImage(it)
            }
        }
    }
}