package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.AdventureView
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.viewModels.AdventureCreationUpdateVM

/**
 * A screen which allows for the viewing of an individual Adventure.
 * @param adventureId the id of the adventure to be viewed on the screen.
 */
@Composable
fun AdventureViewScreen(adventureId: String, adventureVM: AdventureCreationUpdateVM = viewModel()) {
    LaunchedEffect(Unit) {
        adventureVM.setAdventure(adventureId)
    }
    val adventure = adventureVM.adventure.collectAsStateWithLifecycle()

    MainLayout {
        LazyColumn {
            item {
                AdventureView(adventure.value)
            }
        }
    }
}