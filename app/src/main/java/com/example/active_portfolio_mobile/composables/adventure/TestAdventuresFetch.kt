package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.viewModels.AdventureVM

@Composable
fun TestAdventuresFetch(adventureVM: AdventureVM = viewModel()) {
    val adventures by adventureVM.adventures.collectAsStateWithLifecycle()

    Column {
        Button(onClick = {adventureVM.fetchAllAdventures()}) {
            Text("Get All Adventures")
        }
        if (adventures.isNotEmpty()) {
            Text("Total adventures fetched: ${adventures.size}")
        }
    }
}