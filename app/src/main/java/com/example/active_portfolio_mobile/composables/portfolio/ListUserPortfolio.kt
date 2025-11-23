package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.active_portfolio_mobile.navigation.LocalAuthViewModel
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.viewModels.UserPortfolio


/**
 * Displays the list of portfolios for a given user.
 *
 * If the authenticated user is viewing their own profile, all their portfolios
 * are shown. Otherwise, only public portfolios for that user are loaded.
 *
 * @param userId The ID of the user whose portfolios should be displayed.
 * @param userPortfolio The viewModel that provides portfolio data. 
 */
@Composable
fun ListUserPortfolio(userId: String, userPortfolio: UserPortfolio = viewModel()){
    //Current authenticated user
    val authViewModel: AuthViewModel = LocalAuthViewModel.current
    val user = authViewModel.uiState.collectAsStateWithLifecycle().value.user

    //List of portfolios for th viewed user
    val portfolios = userPortfolio.portfolios.collectAsStateWithLifecycle()

    //Navigation controller for editing portfolios.
    val navController = LocalNavController.current

    //Load the correct set of portfolios
    if(userId ==user?.id){
        LaunchedEffect(Unit) {
            userPortfolio.allUserPortfolio(userId)      //Load all portfolios
        }
    }else{
        LaunchedEffect(Unit) {
            userPortfolio.allUserVisibilityPortfolio(userId, "public")      //Load only public portfolio
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        //Header
        Row( 
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(userId == user?.id){
                Text(
                    text = "My Portfolio: ${portfolios.value.size}"
                )
            }else{
                Text(
                    text = "${user?.firstName?.firstOrNull()?.uppercase()?: ""}'s Portfolio: ${portfolios.value.size}"
                )
            }
        }
        //Body - List of Portfolio
        if(portfolios.value.isEmpty()){
            //No portfolio found message
            Text(
                text = "No portfolio yet!"
            )
        }
        else {
            //Render each portfolio item
            portfolios.value.forEach {
                portfolio ->
                Row(                
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Button click to open the portfolio
                    Button(                            
                        modifier = Modifier.width(200.dp),
                        onClick = {
                            //TODO Replace the navigate to the display of the portfolio
                            //navController.navigate(Routes.CreateUpdatePortfolio.goEdit(portfolio!!.id))
                        }){
                            Text(portfolio!!.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    //Show edit button only if the user is viewing their own portfolio
                    if(userId == user?.id){
                        IconButton(
                            onClick = {
                                navController.navigate(Routes.CreateUpdatePortfolio.goEdit(portfolio!!.id))
                            }
                        ){
                            Icon(imageVector = Icons.Filled.ChangeCircle, contentDescription = "Update Portfolio" )
                        }
                    }
                }
            }
        }
    }
}