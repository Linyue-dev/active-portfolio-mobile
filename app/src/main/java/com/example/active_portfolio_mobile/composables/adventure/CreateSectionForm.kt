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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.utilities.rememberMutableStateListOf
import com.example.active_portfolio_mobile.viewModels.AdventureSectionCreationVM
import kotlin.collections.forEach

@Composable
fun CreateSectionForm(type: String, sectionVM: AdventureSectionCreationVM) {
    var label by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var stringContent by rememberSaveable { mutableStateOf("") }
    val imageContent = rememberMutableStateListOf<Bitmap>()
    val portfolios = rememberMutableStateListOf<String>()
    val navController: NavController = LocalNavController.current

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

        // Set the portfolios in which to include this Section.
        // if (parentAdventure.portfolios.isNotEmpty()) {
        // TODO Get the adventure's portfolios and set them here dynamically, using their names
        DropDownTab(name = "Portfolios") {
            MultiSelectList(
                selectedItems = emptyList(),
                list = listOf("Portfolio 1", "Portfolio 2", "Portfolio 3"),
                selectItem = {
                    portfolios.add(it)
                },
                deselectItem = {
                    portfolios.remove(it)
                }
            )
        }
        // }

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
            // Save the section according to its type
            when (type) {
                SectionType.IMAGE -> sectionVM.saveNewImageSection(
                    sectionToSave, 
                    imageContent
                ){
                    success = it
                }
                else -> sectionVM.saveNewSection(sectionToSave) {
                    success = it
                }
            }

            if (success) {
                navController.navigateUp() // ToDo, make sure previous screen updates w/ new content
            } else {
                // Todo, error popup
            }
        }) {
            Text("Save")
        }
    }
}

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