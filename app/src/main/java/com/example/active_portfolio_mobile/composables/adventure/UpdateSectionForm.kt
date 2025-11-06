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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.viewModels.AdventureSectionImageUpdateVM
import com.example.active_portfolio_mobile.viewModels.AdventureSectionUpdateVM

/**
 * A form for updating a Text or Link Adventure Section.
 * @param sectionToShow the section to update with the form.
 * @param parentAdventure the adventure to which the section belongs.
 * @param adventureSectionVM the View Model which contains the list of sections, used for accessing
 * the ActivePortfolio API functions and removing deleted sections from the section list.
 */
@Composable
fun UpdateSectionForm(
    sectionToShow: AdventureSection,
    parentAdventure: Adventure,
    adventureSectionVM: AdventureSectionUpdateVM
) {
    val section = remember { mutableStateOf(sectionToShow) }
    var message by rememberSaveable { mutableStateOf("") }

    Column {
        TextField(
            label = { Text("Label") },
            value = section.value.label,
            onValueChange = { section.value = section.value.copy(label = it) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ), modifier = Modifier.fillMaxWidth()
        )

        when (section.value.type) {
            SectionType.TEXT -> UpdateStringSectionContent("Text", section.value.content) {
                section.value = section.value.copy( content = it)
            }
            SectionType.LINK -> UpdateStringSectionContent("Link", section.value.content) {
                section.value = section.value.copy( content = it)
            }
        }

        // Text type Adventure Sections are the only ones without a description.
        if (section.value.type != SectionType.TEXT) {
            TextField(
                label = { Text("Description") },
                value = section.value.description ?: "",
                onValueChange = { section.value = section.value.copy(description = it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ), modifier = Modifier.fillMaxWidth()
            )
        }

        // if (parentAdventure.portfolios.isNotEmpty()) {
        // TODO Get the adventure's portfolios and set them here dynamically, using their names
        DropDownTab(name = "Portfolios") {
            MultiSelectList(
                selectedItems = section.value.portfolios,
                list = listOf("Portfolio 1", "Portfolio 2", "Portfolio 3"),
                selectItem = {
                    section.value = section.value.copy(portfolios = section.value.portfolios + it)
                },
                deselectItem = {
                    section.value = section.value.copy(portfolios = section.value.portfolios - it)
                }
            )
        }
        // }

        Button(onClick = {
            adventureSectionVM.updateSection(section.value) { newMessage ->
                message = newMessage
                println(message)
            }
        }) {
            Text("Save")
        }

        DeleteButtonWithConfirm {
            adventureSectionVM.deleteSection(section.value) {
                message = it
            }
        }

        if (message != "") {
            Text(message)
        }
    }
}

/**
 * A field for editing a Section's content in the Update Section Form where that content is a string.
 * @param contentType the name to be given to the content field's label, reflecting the content type
 * of the section.
 * @param content the actual content of the section to be updated in the field.
 * @param setSectionContent a setter for updating the section's content.
 */
@Composable
fun UpdateStringSectionContent(contentType: String, content: String, setSectionContent: (String) -> Unit) {
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
 * A form for updating an Image Adventure Section.
 * @param sectionToShow the section to update with the form.
 * @param parentAdventure the adventure to which the section belongs.
 * @param allSectionsVM the View Model which contains the list of sections, used for accessing
 * certain ActivePortfolio API functions and removing deleted sections from the section list.
 * @param adventureSectionVM the View Model for updating an image type Adventure Section. Creates
 * a new View Model for this purpose by default.
 */
@Composable
fun UpdateImageSectionForm(
    sectionToShow: AdventureSection,
    parentAdventure: Adventure,
    allSectionsVM: AdventureSectionUpdateVM,
    adventureSectionVM: AdventureSectionImageUpdateVM = viewModel()
) {
    LaunchedEffect(Unit) {
        adventureSectionVM.setSection(sectionToShow)
    }

    val section by adventureSectionVM.section.collectAsStateWithLifecycle()
    val bitmaps = adventureSectionVM.bitmapImages.collectAsStateWithLifecycle()
    var message by rememberSaveable { mutableStateOf("") }

    Column {
        TextField(
            label = { Text("Label") },
            value = section.label,
            onValueChange = { adventureSectionVM.setLabel(it) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ), modifier = Modifier.fillMaxWidth()
        )

        UpdateImageSectionContent(
            bitmaps.value,
            { adventureSectionVM.addImage(it) },
            { adventureSectionVM.removeImage(it) }
        )

        TextField(
            label = { Text("Description") },
            value = section.description ?: "",
            onValueChange = { adventureSectionVM.setDescription(it) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ), modifier = Modifier.fillMaxWidth()
        )

        // if (parentAdventure.portfolios.isNotEmpty()) {
        // TODO Get the adventure's portfolios and set them here dynamically, using their names
        DropDownTab(name = "Portfolios") {
            MultiSelectList(
                selectedItems = section.portfolios,
                list = listOf("Portfolio 1", "Portfolio 2", "Portfolio 3"),
                selectItem = {
                    adventureSectionVM.addToPortfolios(it)
                },
                deselectItem = {
                    adventureSectionVM.removeFromPortfolios(it)
                }
            )
        }
        // }

        Button(onClick = {
            adventureSectionVM.updateSection { newMessage ->
                message = newMessage
                println(message)
            }
        }) {
            Text("Save")
        }

        DeleteButtonWithConfirm {
            allSectionsVM.deleteSection(section) {
                message = it
            }
            if (message == "Success") {  }
        }

        if (message != "") {
            Text(message)
        }
    }
}

/**
 * A field for editing a Section's content in the Update Section Form when that content is an array
 * of bitmaps (representing image files).
 * @param bitmaps the image files which are the content of the Section being updated.
 * @param addImage a function to be run whenever a new image is added, passing that image bitmap
 * as an argument.
 * @param removeImage a function to be run whenever an image is removed, passing that image bitmap
 * as an argument.
 */
@Composable
fun UpdateImageSectionContent(
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