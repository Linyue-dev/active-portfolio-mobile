package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.composables.adventure.DeleteButtonWithConfirm
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.composables.adventure.MultiSelectList
import com.example.active_portfolio_mobile.composables.adventure.SingleSelectList
import com.example.active_portfolio_mobile.data.remote.dto.Adventure
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.viewModels.AdventureCreationUpdateVM

/**
 * The primary screen for creating, updating, and deleting an Adventure.
 * @param adventureToUpdate The id of an existing Adventure which is to be updated.
 * If none supplied, the screen will seek to create a new adventure.
 */
@Composable
fun CreateAdventureScreen(
    modifier: Modifier,
    adventureToUpdate: String? = null,
    adventureVM: AdventureCreationUpdateVM = viewModel()
) {
    val navController: NavController = LocalNavController.current
    val adventure by adventureVM.adventure.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (adventureToUpdate != null) {
            adventureVM.setAdventure(adventureToUpdate)
        }
        adventureVM.setUserId("68ff6707223ec2d08217d54d") //TODO set to the signed in user's id
    }

    MainLayout {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            // Set Title of Adventure
            item {
                TextField(
                    label = { Text("Title") },
                    value = adventure.title,
                    onValueChange = { adventureVM.setTitle(it) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                    ), modifier = Modifier.fillMaxWidth()
                )
            }
            // Navigate to Adventure Section creation/update/delete for this adventure
            item {
                if (adventure.id != "") {
                    Button(onClick = {
                        navController.navigate(Routes.SectionsUpdate.go(adventure.id))
                    }) {
                        Text("Build My Adventure") //TODO actually nav to section update screen (only when adventure already created)
                    }
                }
            }
            // Set visibility of Adventure
            item {
                DropDownTab(name = "Visibility: ${adventure.visibility}") {
                    SingleSelectList(
                        selectedItem = adventure.visibility,
                        setSelectedItem = { adventureVM.setVisibility(it) },
                        list = listOf("Public", "Private") // TODO switch for less hardcoded values
                    )
                }
            }
            // Select Portfolios in which to include this Adventure.
            item {
                // TODO Get the user's portfolios and set them here dynamically, using their names
                DropDownTab(name = "Portfolios (TO UPDATE WITH REAL PORTFOLIOS)") {
                    MultiSelectList(
                        selectedItems = adventure.portfolios,
                        list = listOf("Portfolio 1", "Portfolio 2", "Portfolio 3"),
                        selectItem = {
                            adventureVM.addToPortfolios(it)
                        },
                        deselectItem = {
                            adventureVM.removeFromPortfolios(it)
                        }
                    )
                }
            }
            // Create Adventure or save changes to existing one.
            item {
                Button(onClick = {
                    adventureVM.saveAdventure()
                }) {
                    Text("Save")
                }
                if (adventureVM.message.value != null) {
                    Text("${adventureVM.message.value}")
                }
            }
            // Delete the Adventure.
            item {
                if (adventure.id != "") {
                    DeleteButtonWithConfirm {
                        adventureVM.deleteAdventure()
                    }
                }
            }
        }
    }
}