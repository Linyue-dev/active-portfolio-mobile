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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.composables.adventure.AdventureView
import com.example.active_portfolio_mobile.composables.portfolio.DescriptionPanel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.viewModels.GetPortfoliosVM
import com.example.active_portfolio_mobile.viewModels.PortfolioMV

/**
 * Displays a complete portfolio page containing
 * the portfolio title and description. And then in the center
 * it displays the adventures linked to that portfolio.
 * You can copy the link of the portfolio to share it (not done yet).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayPortfolioPage(portfolioId : String, getPortfolioMV: GetPortfoliosVM = viewModel(), portfolioMV: PortfolioMV = viewModel()){
    
    //Navigation and context access
    val navController = LocalNavController.current
    val context = LocalContext.current

    //Collect viewmodel state for both the portfolio and the adventures
    val portfolio by getPortfolioMV.portfolio.collectAsStateWithLifecycle()
    val isPortfolioLoading by getPortfolioMV.isLoading.collectAsStateWithLifecycle()
    val portfolioError by getPortfolioMV.errorMessage.collectAsStateWithLifecycle()

    val adventures by portfolioMV.adventures.collectAsStateWithLifecycle()
    val isAdventureLoading by portfolioMV.isLoading.collectAsStateWithLifecycle()
    val adventureError by portfolioMV.errorMessage.collectAsStateWithLifecycle()


    //When this composable loads or the Id changes, fetch the portfolio
    //and its adventures.
    LaunchedEffect(portfolioId) {
        getPortfolioMV.loadOnePortfolio(portfolioId)
        portfolioMV.loadAdventureFromPortfolio(portfolioId)
    }

    //Controls whether the description panel appears or no.
    var detailsExpended by remember { mutableStateOf(false) }
    Scaffold(
        //TOP BAR: Shows the tile and back button.
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
        },
        //BOTTOM BAR: Expand the description and the share button
        bottomBar = {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                BottomAppBar(
                    actions = {
                        // + button : Toggle the description panel
                        IconButton(onClick = {detailsExpended = !detailsExpended}) {
                            Icon(
                                imageVector = if(detailsExpended) Icons.Default.Remove else Icons.Default.Add,
                                contentDescription = "Toggle Description"
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        //Share button
                        IconButton(
                            onClick = {
                                //Copies a portfolio link to clipboard
                                //TODO CHANGE THE LINK TO BE THE PROPER LINK
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
            
            //Main content
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                //Loading state for portfolio and adventure list.
                //Show a loading circle.
                if(isPortfolioLoading || isAdventureLoading){
                    Box(
                        modifier = Modifier
                            .fillMaxSize() ,
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                    return@Column
                }
                //Display portfolio or adventures errors
                //TODO remake it prettier
                if(portfolioError != null){
                    Text("Error: $portfolioError")
                }
                if(adventureError != null){
                    Text("Error: $adventureError")
                }

                //Pager to horizontally swipe through adventures
                val pagerState = rememberPagerState(pageCount = {adventures.size})

                //Reset page if the list size shrinks
                LaunchedEffect(adventures.size) {
                    if(pagerState.currentPage >= adventures.size){
                        pagerState.scrollToPage(0)
                    }
                }

                //Show adventures or fallback text
                if(adventures.isNotEmpty()){

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                            beyondViewportPageCount = 1,
                            key = { page -> adventures[page].id },
                        ) { page ->
                            val adventure = adventures[page]

                            //Display a single adventure page
                            AdventureView(
                                adventureToView = adventure,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                filterPortfolioId = portfolioId
                            )
                        
                    }
                }
                else {
                    Text("No adventures in this portfolio yet")
                }
            }
            
            
            //Description Box (slides up and down)
            DescriptionPanel(
                visible = detailsExpended,
                description = portfolio?.description ?: ""
            )
        }
    }
}