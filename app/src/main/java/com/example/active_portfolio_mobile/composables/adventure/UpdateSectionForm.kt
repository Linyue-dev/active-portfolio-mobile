package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.data.remote.dto.AdventureSection
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.utilities.rememberMutableStateListOf
import com.example.active_portfolio_mobile.viewModels.AdventureSectionUpdateVM
import kotlin.text.Typography.section

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
            SectionType.TEXT -> UpdateTextSectionContent(section.value.content) {
                section.value = section.value.copy( content = it)
            }
            SectionType.LINK -> UpdateLinkSectionContent(section.value.content) {
                section.value = section.value.copy( content = it)
            }
            SectionType.IMAGE -> {
                var imageSectionContent = rememberMutableStateListOf(ImageBitmap)
                UpdateImageSectionContent(section.value) {
                    imageSectionContent = it
                }
            }
        }
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

@Composable
fun UpdateTextSectionContent(content: String, setSectionContent: (String) -> Unit) {
    TextField(
        label = { Text("Text") },
        value = content,
        onValueChange = setSectionContent,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ), modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun UpdateLinkSectionContent(content: String, setSectionContent: (String) -> Unit) {
    TextField(
        label = { Text("Link URL") },
        value = content,
        onValueChange = setSectionContent,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ), modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun UpdateImageSectionContent(
    section: AdventureSection,
    setSectionContent: (SnapshotStateList<ImageBitmap.Companion>) -> Unit
) {
// TODO

//    TextField(
//        label = { Text("Images") },
//        value = section.content,
//        onValueChange = { setSectionContent(it) },
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = MaterialTheme.colorScheme.surface,
//            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
//        ), modifier = Modifier.fillMaxWidth()
//    )
}