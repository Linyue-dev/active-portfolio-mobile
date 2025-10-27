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
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.composables.adventure.MultiSelectList
import com.example.active_portfolio_mobile.composables.adventure.SingleSelectList
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.model.Adventure
import com.example.active_portfolio_mobile.viewModels.AdventureCreationUpdateVM

@Composable
fun CreateAdventureScreen(
    modifier: Modifier,
    adventureToUpdate: Adventure? = null,
    adventureVM: AdventureCreationUpdateVM = viewModel()
) {
    val adventure by adventureVM.adventure.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        adventureVM.setUserId("68ff6707223ec2d08217d54d") //TODO set to the signed in user's id
    }

    MainLayout {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
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
            item {
                Button(onClick = {}) {
                    Text("Build My Adventure") //TODO actually nav to section update screen
                }
            }
            item {
                DropDownTab(name = "Visibility: ${adventure.visibility}") {
                    SingleSelectList(
                        selectedItem = adventure.visibility,
                        setSelectedItem = { adventureVM.setVisibility(it) },
                        list = listOf("Public", "Private") // TODO switch for less hardcoded values
                    )
                }
            }
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
        }
    }
}