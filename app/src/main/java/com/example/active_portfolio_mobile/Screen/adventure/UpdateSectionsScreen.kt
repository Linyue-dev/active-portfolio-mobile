package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.composables.adventure.UpdateImageSectionForm
import com.example.active_portfolio_mobile.composables.adventure.UpdateSectionForm
import com.example.active_portfolio_mobile.data.remote.dto.SectionType
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.viewModels.AdventureCreationUpdateVM
import com.example.active_portfolio_mobile.viewModels.AdventureSectionUpdateVM
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * The screen for updating and deleting the existing sections of a specific adventure.
 * @param adventureId the id of the adventure whose sections are to be updated.
 * @param adventureSectionVM
 * @param adventureVM
 */
@Composable
fun UpdateSectionsScreen(
    adventureId: String,
    adventureSectionVM: AdventureSectionUpdateVM = viewModel(),
    adventureVM: AdventureCreationUpdateVM = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val messageFlow = remember { MutableSharedFlow<String>() }

    // Using a counter for updated, triggers launched effect whenever incremented.
    var updated by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(updated) {
        // Refetches the sections when one is updated, so they are always up to date.
        adventureSectionVM.fetchSections(adventureId)
    }
    LaunchedEffect(Unit) {
        adventureSectionVM.fetchPortfolios(adventureId)
        adventureVM.setAdventure(adventureId)

        // Collect messages emitted for the snackbar.
        messageFlow.collect { message ->
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }
    val navController: NavController = LocalNavController.current
    val sections = adventureSectionVM.sections
    val adventure by adventureVM.adventure.collectAsStateWithLifecycle()

    MainLayout {
        Box( modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(5.dp),
            ) {
                item {
                    Text(adventure.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
                // List all the sections in the adventure.
                items(sections.value, key = { it.id }) { section ->
                        when (section.type) {
                            SectionType.IMAGE -> DropDownTab(section.label) {
                                UpdateImageSectionForm(
                                    sectionToShow = section,
                                    allSectionsVM = adventureSectionVM,
                                    messageFlow = messageFlow,
                                    adventureSectionVM = viewModel(key = section.id)
                                ) {
                                    updated++
                                }
                            }

                            else -> DropDownTab(section.label) {
                                UpdateSectionForm(section, messageFlow, adventureSectionVM) {
                                    updated++
                                }
                            }
                        }
                }
                item {
                    Button(onClick = {
                        navController.navigate(Routes.SectionsCreate.go(adventureId))
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ), border = BorderStroke(width = 2.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ), modifier = Modifier.padding(top = 15.dp)
                    ) {
                        Text("+ New Section")
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