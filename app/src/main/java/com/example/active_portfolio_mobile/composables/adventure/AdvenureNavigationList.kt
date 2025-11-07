package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.utilities.canViewWithRole
import com.example.active_portfolio_mobile.viewModels.AdventureVM

@Composable
fun AdventureNavigationList(
    userId: String,
    adventureVM: AdventureVM = viewModel()
) {
    LaunchedEffect(Unit) {
        adventureVM.fetchAdventuresByUser(userId)
    }
    val adventures = adventureVM.adventures.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val authViewModel = LocalAuthViewModel.current
    val userAuthState = authViewModel.uiState.collectAsStateWithLifecycle().value

    Column {
        adventures.value.forEach { adventure ->
            // Only display adventures if the signed in user is their creator,
            // or if they have the right role for the adventure's visibility level.
            if ( userAuthState.user?.id == adventure.userId
                || canViewWithRole(userAuthState.user?.role ?: "public", adventure.visibility)
                ) {
                Row {
                    Button(
                        modifier = Modifier.width(200.dp),
                        onClick = {
                        navController.navigate(Routes.AdventureView.go(adventure.id))
                    }) {
                        Text(adventure.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    // Only provide the "Update" button if the signed in user is the adventure's
                    // creator or if they are an admin.
                    if (userAuthState.user?.id == userId || userAuthState.user?.role == "admin") {
                        Button(onClick = {
                            navController.navigate(Routes.AdventureUpdate.go(adventure.id))
                        }) {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}