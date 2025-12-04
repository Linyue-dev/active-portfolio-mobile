package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.utilities.canViewWithRole
import com.example.active_portfolio_mobile.viewModels.AdventureVM

/**
 * A list of buttons for navigating to the view and update pages of a specified user's adventures.
 * The adventures which appear are based on the signed in user's role compared to the adventure's
 * visibility level. The button to navigate to the update page is only accessible by the
 * adventure's creator and admin users.
 * @param userId the id of the user whose created adventures are to be listed.
 */
@Composable
fun AdventureNavigationList(
    userId: String,
    adventureVM: AdventureVM = viewModel()
) {
    LaunchedEffect(userId) {
        adventureVM.fetchAdventuresByUser(userId)
    }
    val adventures = adventureVM.adventures.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val authViewModel = LocalAuthViewModel.current
    val userAuthState = authViewModel.uiState.collectAsStateWithLifecycle().value

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        // =============== Header ===============
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "My Adventure",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${adventures.value.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // =============== Adventure list ===============
        if (adventures.value.isEmpty()){
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Source,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No Adventures Yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else{
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
                            }, modifier = Modifier.padding(start = 40.dp)
                            ) {
                                Text("Update")
                            }
                        }
                    }
                }
            }
        }
    }
}