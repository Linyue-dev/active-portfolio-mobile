package com.example.active_portfolio_mobile.Screen.adventure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.CreateSectionForm
import com.example.active_portfolio_mobile.composables.adventure.DropDownTab
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.viewModels.AdventureSectionCreationVM
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * The primary screen for creating new sections of an adventure.
 * @param adventureId the id of the adventure to which the created section will belong.
 */
@Composable
fun CreateSectionScreen(
    adventureId: String,
    adventureSectionVM: AdventureSectionCreationVM = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val messageFlow = remember { MutableSharedFlow<String>() }
    LaunchedEffect(Unit) {
        adventureSectionVM.setAdventureId(adventureId)
        adventureSectionVM.fetchPortfolios(adventureId)

        // Collect messages emitted for the snackbar.
        messageFlow.collect { message ->
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }

    MainLayout {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            LazyColumn {
                item {
                    DropDownTab("New Text Section") {
                        CreateSectionForm("text", messageFlow, adventureSectionVM)
                    }
                }
                item {
                    DropDownTab("New Link Section") {
                        CreateSectionForm("link", messageFlow, adventureSectionVM)
                    }
                }
                item {
                    DropDownTab("New Image Section") {
                        CreateSectionForm("image", messageFlow, adventureSectionVM)
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