package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

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
    val snackbarHostState = remember { SnackbarHostState() }
    val messageFlow = remember { MutableSharedFlow<String>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (adventureToUpdate != null) {
            adventureVM.setAdventure(adventureToUpdate)
        }

        // Collect messages emitted for the snackbar.
        messageFlow.collect { message ->
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }
    LaunchedEffect(user) {
        adventureVM.setUserId(user?.id ?: "")
    }

    MainLayout {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                // Set Title of Adventure
                item {
                    TextField(
                        label = { Text("Title", style = MaterialTheme.typography.titleMedium) },
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
                        }, modifier = Modifier.padding(top = 20.dp)
                        ) {
                            Text("Build My Adventure",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(horizontal = 15.dp)
                                    .padding(vertical = 10.dp)
                            )
                        }
                    }
                }
                // Set visibility of Adventure
                item {
                    DropDownTab(name = "Visibility: ${adventure.visibility}") {
                        SingleSelectList(
                            selectedItem = adventure.visibility,
                            setSelectedItem = { adventureVM.setVisibility(it) },
                            list = listOf(
                                "Public",
                                "Private"
                            )
                        )
                    }
                }
                // Select Portfolios in which to include this Adventure.
                item {
                    DropDownTab(name = "Portfolios") {
                        if (portfolios.value.isNotEmpty()) {
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
                        } else {
                            Text("You do not currently have any portfolios",
                                modifier = Modifier.padding(20.dp))
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.padding(top = 50.dp).fillMaxWidth()
                    ) {
                        // Create Adventure or save changes to existing one.
                        IconButton(
                            onClick = {
                                adventureVM.saveAdventure(authViewModel.tokenManager.getToken()) {
                                    scope.launch {
                                        messageFlow.emit(it)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Save new or updated Adventure",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        // Delete the Adventure
                        if (adventure.id != "") {
                            DeleteButtonWithConfirm {
                                adventureVM.deleteAdventure(authViewModel.tokenManager.getToken()) {
                                    scope.launch {
                                        messageFlow.emit(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}