package com.example.active_portfolio_mobile.Screen.portfolio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.viewModels.GetPortfoliosVM


@Composable
fun DisplayPortfolioPage(portfolioId : String, getPortfolioMV: GetPortfoliosVM = viewModel()){
    val portfolio by getPortfolioMV.portfolio.collectAsStateWithLifecycle()
    val isLoading by getPortfolioMV.isLoading.collectAsStateWithLifecycle()
    val errorMessage by getPortfolioMV.errorMessage.collectAsStateWithLifecycle()
    getPortfolioMV.loadOnePortfolio(portfolioId)
    
    Column(modifier = Modifier.padding(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        when {
            isLoading -> Text("Loading...")
            errorMessage != null -> Text("Error: $errorMessage")
            portfolio != null && portfolio!!.title.isNotBlank() -> Text("Portfolios title: ${portfolio!!.title}")
            else -> Text("No portfolios yet")
        }
    }
}