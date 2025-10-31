package com.example.active_portfolio_mobile.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel

@Composable
fun ProfilePage(
    viewModel: AuthViewModel
){
    MainLayout {
        val uiState by viewModel.uiState.collectAsState()
        val navController = LocalNavController.current
        LaunchedEffect(uiState.isLoggedIn) {
            if (!uiState.isLoggedIn){
                navController.navigate(Routes.Login.route){
                    popUpTo(Routes.Profile.route) {inclusive = true}
                }
            }
        }

        Column {
            Text("welcome")
            Button(onClick = { viewModel.logout() }) {
                Text("Logout")
            }
        }
    }
}