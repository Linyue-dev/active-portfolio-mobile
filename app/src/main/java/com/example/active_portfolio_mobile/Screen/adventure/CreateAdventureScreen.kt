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
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
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
    val authViewModel: AuthViewModel = LocalAuthViewModel.current
    val user = authViewModel.uiState.collectAsStateWithLifecycle().value.user
    val navController: NavController = LocalNavController.current
    val adventure by adventureVM.adventure.collectAsStateWithLifecycle()
    val portfolios = adventureVM.portfolios
    LaunchedEffect(Unit) {
        if (adventureToUpdate != null) {
            adventureVM.setAdventure(adventureToUpdate)
        }
    }
    LaunchedEffect(user) {
        adventureVM.setUserId(user?.id ?: "")
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
                        Text("Build My Adventure")
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
                DropDownTab(name = "Portfolios") {
                    MultiSelectList(
                        selectedItems = portfolios.value.filter { it.id in adventure.portfolios },
                        list = portfolios.value,
                        displayText = { it.title },
                        selectItem = {
                            adventureVM.addToPortfolios(it.id)
                        },
                        deselectItem = {
                            adventureVM.removeFromPortfolios(it.id)
                        }
                    )
                }
            }
            // Create Adventure or save changes to existing one.
            item {
                Button(onClick = {
                    adventureVM.saveAdventure(authViewModel.tokenManager.getToken())
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
                        adventureVM.deleteAdventure(authViewModel.tokenManager.getToken())
                    }
                }
            }
        }
    }
}