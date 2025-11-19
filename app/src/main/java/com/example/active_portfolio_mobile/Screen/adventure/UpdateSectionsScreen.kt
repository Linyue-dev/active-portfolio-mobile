package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    LaunchedEffect(Unit) {
        adventureSectionVM.fetchSections(adventureId)
        adventureSectionVM.fetchPortfolios(adventureId)
        adventureVM.setAdventure(adventureId)
    }
    val navController: NavController = LocalNavController.current
    val sections = adventureSectionVM.sections
    val adventure by adventureVM.adventure.collectAsStateWithLifecycle()

    MainLayout {
        LazyColumn {
            item {
                Text(adventure.title)
            }
            // List all the sections in the adventure.
            items(sections.value, key = {it.id}) { section ->
                when (section.type) {
                    SectionType.IMAGE -> DropDownTab(section.label) {
                        UpdateImageSectionForm(
                            sectionToShow = section,
                            allSectionsVM = adventureSectionVM,
                            adventureSectionVM = viewModel(key = section.id)
                        )
                    }
                    else -> DropDownTab(section.label) {
                        UpdateSectionForm(section, adventureSectionVM)
                    }
                }
            }
            item {
                Button(onClick = {
                    navController.navigate(Routes.SectionsCreate.go(adventureId))
                }) {
                    Text("Add New Section")
                }
            }
        }
    }
}