package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.viewModels.AdventureSectionCreationVM

@Composable
fun CreateSectionScreen(
    adventureId: String,
    adventureSectionVM: AdventureSectionCreationVM = viewModel()
) {
    LaunchedEffect(Unit) {
        adventureSectionVM.setAdventureId(adventureId)
    }

    MainLayout {
        LazyColumn {
            item {
                DropDownTab("New Text Section") {

                }
            }

        }
    }
}