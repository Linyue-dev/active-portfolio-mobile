package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.viewModels.PortfoliosVM


//TO COMPLETELY REDO
@Composable
fun GetAllPortfolio(portfoliosVM: PortfoliosVM = viewModel()) {
    val portfolios by portfoliosVM.portfolios.collectAsStateWithLifecycle()
    val isLoading by portfoliosVM.isLoading.collectAsStateWithLifecycle()
    val errorMessage by portfoliosVM.errorMessage.collectAsStateWithLifecycle()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { portfoliosVM.loadAllPortfolio() }) {
            Text("Get All Portfolio")
        }

        when {
            isLoading -> Text("Loading...")
            errorMessage != null -> Text("Error: $errorMessage")
            portfolios.isNotEmpty() -> Text("Total portfolios fetched: ${portfolios.size}")
            else -> Text("No portfolios yet")
        }
    }
}