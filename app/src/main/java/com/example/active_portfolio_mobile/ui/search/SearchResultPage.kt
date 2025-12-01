package com.example.active_portfolio_mobile.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes

/**
 * Displays the search result page for user lookup.
 *
 * This screen:
 * - Initializes the search query from navigation arguments.
 * - Executes a user search when the query changes or when triggered by the search bar.
 * - Observes [SearchViewModel.uiState] to display loading, error, empty state, or results.
 * - Navigates to another user's profile when a user card is selected.
 *
 * @param initialQuery The initial search text passed from the previous screen.
 * @param viewModel The [SearchViewModel] responsible for handling search logic and exposing UI state.
 */
@Composable
fun SearchResultPage(
    initialQuery: String,
    viewModel: SearchViewModel
){
    var searchQuery by rememberSaveable { mutableStateOf(initialQuery) }
    val navController = LocalNavController.current
    val uiState by viewModel.uiState.collectAsState()
    val hasInitialized = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if(!hasInitialized.value && initialQuery.isNotEmpty()){
            searchQuery = initialQuery
            viewModel.searchUsers(initialQuery)
            hasInitialized.value = true
        }
    }

    MainLayout {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                //============== Search Bar ==============
                SearchBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        if (it.length >= 2) {
                            viewModel.searchUsers(it)
                        } else{
                            viewModel.clearSearch()
                        }
                    },
                    onSearch = {
                        if (searchQuery.length >= 2) {
                            viewModel.searchUsers(searchQuery)
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                //============== Search Result ==============
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    !uiState.isLoading && uiState.results.isEmpty() && searchQuery.length >= 2 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "No username found",  // ← 也改成 "No"
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.results) { user ->
                                UserCard(
                                    user = user,
                                    onClick = {
                                        val identifier = user.username ?: user.id
                                        navController.navigate(Routes.OtherUserProfile.go(identifier))

                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}