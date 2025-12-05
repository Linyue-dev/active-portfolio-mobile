package com.example.active_portfolio_mobile.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.active_portfolio_mobile.composables.adventure.AdventureView
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.ui.search.SearchBar
import com.example.active_portfolio_mobile.viewModels.AdventureVM

//Display the landing page of the app with the tile and navigation button.
//Users can navigate to the comments page or the About us page from here.
@Composable
fun LandingPage(modifier : Modifier, adventureVM: AdventureVM = viewModel(key = "landing")){
    val navController: NavController = LocalNavController.current
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        adventureVM.fetchRecentPublicAdventures(5)
    }
    val recentAdventures = adventureVM.adventures.collectAsStateWithLifecycle()

    MainLayout {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Active Portfolio",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ============== Search Bar ==============
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        if (searchQuery.isNotEmpty()){
                            Log.d("LandingPage", "Navigating with query: '$searchQuery'")
                            navController.navigate(Routes.SearchUser.go(searchQuery))
                        }
                    }
                )

                Spacer(Modifier.padding(top = 20.dp))
                Button(
                    onClick = { navController.navigate(Routes.Info.route) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = "How To Use This App",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    //View comment button
//                    Button(
//                        onClick = { navController.navigate(Routes.Comment.route) },
//                        shape = RoundedCornerShape(12.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
//                    ) {
//                        Text(
//                            text = "View comments",
//                            color = MaterialTheme.colorScheme.onSurface,
//                            fontSize = 16.sp
//                        )
//                    }
                    // ============== About Us Button ==============
                    Button(
                        onClick = { navController.navigate(Routes.About.route) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "About us!!",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    }
                }

                if (recentAdventures.value.isNotEmpty()) {
                    Spacer(Modifier.padding(vertical = 20.dp))
                    Text("Check out these Public Adventures!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    recentAdventures.value.forEach { adventure ->
                        AdventureView(adventure)
                    }
                }

                // ============== Demo buttons ==============
//                Button(onClick = {
//                    navController.navigate(Routes.AdventureUpdate.go("6915f42db4bdb25ac6a42fae"))
//                }) {
//                    Text("Demo Adventure Update")
//                }
//                Button(onClick = {
//                    navController.navigate(Routes.AdventureView.go("6915f42db4bdb25ac6a42fae"))
//                }) {
//                    Text("Demo Adventure View")
//                }
            }
        }
    }
}