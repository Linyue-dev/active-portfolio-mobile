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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.viewModels.AdventureSectionImageUpdateVM
import com.example.active_portfolio_mobile.viewModels.AdventureSectionUpdateVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * A form for updating a Text or Link Adventure Section.
 * @param sectionToShow the section to update with the form.
 * @param adventureSectionVM the View Model which contains the list of sections, used for accessing
 * the ActivePortfolio API functions and removing deleted sections from the section list.
 */
@Composable
fun UpdateSectionForm(
    sectionToShow: AdventureSection,
    messageFlow: MutableSharedFlow<String>,
    adventureSectionVM: AdventureSectionUpdateVM,
    setUpdated: () -> Unit
) {
    val parentPortfolios = adventureSectionVM.portfolios
    val section = remember { mutableStateOf(sectionToShow) }
    val authViewModel = LocalAuthViewModel.current
    val scope = rememberCoroutineScope()

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

        /**
         * Upon team reflection, we determined that setting portfolios for sections was excessive
         * and confusing. We have therefore decided to simply have all sections appear in a
         * portfolio that includes their parent adventure.
         */
//        if (parentPortfolios.value.isNotEmpty()) {
//            DropDownTab(name = "Portfolios") {
//                MultiSelectList(
//                    selectedItems = parentPortfolios.value.filter { it.id in section.value.portfolios },
//                    list = parentPortfolios.value,
//                    displayText = { it.title },
//                    selectItem = {
//                        section.value = section.value.copy(portfolios = section.value.portfolios + it.id)
//                    },
//                    deselectItem = {
//                        section.value = section.value.copy(portfolios = section.value.portfolios - it.id)
//                    }
//                )
//            }
//        }

        Button(onClick = {
            adventureSectionVM.updateSection(
                section.value,
                authViewModel.tokenManager.getToken()
            ) { newMessage ->
                scope.launch {
                    messageFlow.emit(newMessage)
                }
                println(newMessage)
                setUpdated()
            }
        }) {
            Text("Save")
        }

        DeleteButtonWithConfirm {
            adventureSectionVM.deleteSection(
                section.value,
                authViewModel.tokenManager.getToken()
            ) {
                scope.launch {
                    messageFlow.emit(it)
                }
            }
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
 * @param allSectionsVM the View Model which contains the list of sections, used for accessing
 * certain ActivePortfolio API functions and removing deleted sections from the section list.
 * @param adventureSectionVM the View Model for updating an image type Adventure Section. Creates
 * a new View Model for this purpose by default.
 */
@Composable
fun UpdateImageSectionForm(
    sectionToShow: AdventureSection,
    messageFlow: MutableSharedFlow<String>,
    allSectionsVM: AdventureSectionUpdateVM,
    adventureSectionVM: AdventureSectionImageUpdateVM = viewModel(),
    setUpdated: () -> Unit
) {
    LaunchedEffect(Unit) {
        adventureSectionVM.setSection(sectionToShow)
    }
    val scope = rememberCoroutineScope()
    val parentPortfolios = allSectionsVM.portfolios
    val section by adventureSectionVM.section.collectAsStateWithLifecycle()
    val bitmaps = adventureSectionVM.bitmapImages.collectAsStateWithLifecycle()
    val authViewModel = LocalAuthViewModel.current

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

        /**
         * Upon team reflection, we determined that setting portfolios for sections was excessive
         * and confusing. We have therefore decided to simply have all sections appear in a
         * portfolio that includes their parent adventure.
         */
//        if (parentPortfolios.value.isNotEmpty()) {
//            DropDownTab(name = "Portfolios") {
//                MultiSelectList(
//                    selectedItems = parentPortfolios.value.filter { it.id in section.portfolios },
//                    list = parentPortfolios.value,
//                    displayText = { it.title },
//                    selectItem = {
//                        adventureSectionVM.addToPortfolios(it.id)
//                    },
//                    deselectItem = {
//                        adventureSectionVM.removeFromPortfolios(it.id)
//                    }
//                )
//            }
//        }

        Button(onClick = {
            adventureSectionVM.updateSection(
                authViewModel.tokenManager.getToken()
            ) { newMessage ->
                scope.launch {
                    messageFlow.emit(newMessage)
                }
                println(newMessage)
                setUpdated()
            }
        }) {
            Text("Save")
        }

        DeleteButtonWithConfirm {
            allSectionsVM.deleteSection(
                section = section,
                token = authViewModel.tokenManager.getToken()
            ) {
                scope.launch {
                    messageFlow.emit(it)
                }
            }
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