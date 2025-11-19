package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.CreateSectionForm
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.viewModels.AdventureSectionCreationVM

/**
 * The primary screen for creating new sections of an adventure.
 * @param adventureId the id of the adventure to which the created section will belong.
 */
@Composable
fun CreateSectionScreen(
    adventureId: String,
    adventureSectionVM: AdventureSectionCreationVM = viewModel()
) {
    LaunchedEffect(Unit) {
        adventureSectionVM.setAdventureId(adventureId)
        adventureSectionVM.fetchPortfolios(adventureId)
    }

    MainLayout {
        LazyColumn {
            item {
                DropDownTab("New Text Section") {
                    CreateSectionForm("text", adventureSectionVM)
                }
            }
            item {
                DropDownTab("New Link Section") {
                    CreateSectionForm("link", adventureSectionVM)
                }
            }
            item {
                DropDownTab("New Image Section") {
                    CreateSectionForm("image", adventureSectionVM)
                }
            }
        }
    }
}