package com.example.active_portfolio_mobile.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.active_portfolio_mobile.navigation.LocalNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import com.example.active_portfolio_mobile.navigation.Routes

/**
 * The main layout to be shared across screens.
 * Has a top bar with a back button and a bottom bar with navigation buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(content: @Composable () -> Unit) {
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("JAC Active Portfolio") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior { true },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {

            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        IconButton(onClick = {
                            navController.navigate(route = Routes.Main.route)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home"
                            )
                        }
                        IconButton( onClick = {
                            navController.navigate(Routes.Create.route)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Create a new Adventure or Portfolio"
                            )
                        }
                        IconButton( onClick = {
                            navController.navigate(Routes.Profile.route)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // The main content block
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            content()
        }
    }
}