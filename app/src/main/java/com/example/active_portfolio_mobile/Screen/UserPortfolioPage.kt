package com.example.active_portfolio_mobile.Screen

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.portfolio.ListUserPortfolio
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.viewModels.UserPortfolio
import kotlinx.serialization.internal.throwMissingFieldException

/**
 * Displays a dedicated page showing all portfolios for a specific user.
 *
 * This page uses the ListUserPortfolio component to render the user's portfolio collection.
 * If viewing own profile, shows all portfolios. Otherwise, shows only public portfolios.
 *
 * @param userId The ID of the user whose portfolios should be displayed
 * @param userPortfolio ViewModel that manages portfolio data and API calls
 */
@Composable
fun UserPortfolioPage(
    userId: String,
    userPortfolio: UserPortfolio = viewModel()
){
    MainLayout {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                text = "Portfolio",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ListUserPortfolio(userId, userPortfolio)
        }
    }
}