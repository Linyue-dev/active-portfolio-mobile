package com.example.active_portfolio_mobile.Screen.portfolio

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.content.ClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.AdventureView
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.viewModels.GetPortfoliosVM
import com.example.active_portfolio_mobile.viewModels.PortfolioMV


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayPortfolioPage(portfolioId : String, getPortfolioMV: GetPortfoliosVM = viewModel(), portfolioMV: PortfolioMV = viewModel()){
    val navController = LocalNavController.current
    val context = LocalContext.current
    
    val portfolio by getPortfolioMV.portfolio.collectAsStateWithLifecycle()
    val isPortfolioLoading by getPortfolioMV.isLoading.collectAsStateWithLifecycle()
    val portfolioError by getPortfolioMV.errorMessage.collectAsStateWithLifecycle()

    val adventures by portfolioMV.adventures.collectAsStateWithLifecycle()
    val isAdventureLoading by portfolioMV.isLoading.collectAsStateWithLifecycle()
    val adventureError by portfolioMV.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(portfolioId) {
        getPortfolioMV.loadOnePortfolio(portfolioId)
        portfolioMV.loadAdventureFromPortfolio(portfolioId)
    }

    var detailsExpended by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(portfolio?.title ?: "Portfolio") },
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
        }, bottomBar = {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                if(detailsExpended && portfolio?.description?.isNotBlank() == true){
                    Text(
                        text = portfolio!!.description!!,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                BottomAppBar(
                    actions = {
                        // + button
                        IconButton(onClick = {detailsExpended = !detailsExpended}) {
                            Icon(
                                imageVector = if(detailsExpended) Icons.Default.Remove else Icons.Default.Add,
                                contentDescription = "Toggle Details"
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        //Share button
                        IconButton(
                            onClick = {
                                //CHANGE THE LINK TO BE THE PROPER LINK
                                val link = "google.com"
                                val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
                                clipboard?.setPrimaryClip(ClipData.newPlainText("Portfolio Link", link))
                                Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                            }
                        ){
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                        }
                    }
                )
            }
        }){
            padding  ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //Portfolio loading/error
                when{
                    isPortfolioLoading -> Text("Loading portfolio....")
                    portfolioError != null -> Text("Error: $portfolioError")
                }
                //Adventures loading/error
                when{
                    isAdventureLoading -> Text("Loading adventures....")
                    adventureError != null -> Text("Error: $adventureError")
                }
                val pagerState = rememberPagerState(pageCount = {adventures.size})

                LaunchedEffect(adventures) {
                    pagerState.scrollToPage(0)
                }
                if(adventures.isNotEmpty()){
       
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        key = {page -> adventures[page].id}
                    ){
                        page ->
                        val adventure = adventures[page]
                        AdventureView(
                            adventureToView = adventure,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            filterPortfolioId = portfolioId
                        )
                        
                    }
                }
                else if(!isAdventureLoading){
                    Text("No adventures in this portfolio yet")
                }
            }
    }
}