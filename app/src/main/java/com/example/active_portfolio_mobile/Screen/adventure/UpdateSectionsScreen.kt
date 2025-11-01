package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.model.Adventure
import com.example.active_portfolio_mobile.viewModels.AdventureSectionUpdateVM


@Composable
fun UpdateSectionsScreen(
    adventure: Adventure,
    adventureSectionVM: AdventureSectionUpdateVM = viewModel()
) {
    LaunchedEffect(Unit) {
        adventureSectionVM.setAdventureId(adventure.id)
        adventureSectionVM.fetchSections()
    }
    val sections = adventureSectionVM.sections

    LazyColumn {
        item{
            Text(adventure.title)
        }
        items(sections.value) { item ->
            DropDownTab(item.label) {

            }
        }
    }
}