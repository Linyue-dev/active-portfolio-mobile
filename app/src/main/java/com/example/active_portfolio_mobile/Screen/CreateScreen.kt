package com.example.active_portfolio_mobile.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes

/**
 * A screen for navigating to either the Create Adventure or Create Portfolio screens.
 * This allows us to have a generic "create" button on the bottom nav bar with this page
 * serving to specify what the user wishes to create.
 */
@Composable
fun CreateScreen() {
    val navController: NavController = LocalNavController.current

    MainLayout {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text("Please Select an Option")
            }
            item {
                Button(onClick = {
                    navController.navigate(Routes.AdventureCreate.route)
                }) {
                    Text("Create new Adventure")
                }
            }
            item {
                Button(onClick = {
                    navController.navigate(Routes.CreateUpdatePortfolio.goCreate())
                }) {
                    Text("Create new Portfolio")
                }
            }
        }
    }
}