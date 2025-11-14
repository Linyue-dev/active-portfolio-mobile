package com.example.active_portfolio_mobile.ui.profile

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.viewModels.UserPortfolio
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(
    viewModel: AuthViewModel
){
    val localContext = LocalContext.current
    val activity = localContext as ComponentActivity
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val user = uiState.user
    val userPortfolio : UserPortfolio = viewModel()
    LaunchedEffect(uiState.isLoggedIn) {
        if (!uiState.isLoggedIn){
            navController.navigate(Routes.Login.route){
                popUpTo(0) {inclusive = true}
            }
        }
    }

    if (!uiState.isLoggedIn){
        return
    }

    if(user == null){
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        return
    }
    MainLayout {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row {
                AsyncImage(
                    model = user.profilePicture,
                    contentDescription = "profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(15.dp))

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ){
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(10.dp)
                    )

                    Text(
                        text = "${user.role}",
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )

                    Text(
                        text = "${user.email}",
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                }
            }
            // Button to return the user's name and email to the log-in launcher app.
            // Only appears if the app is launched from the right launcher.
            if (activity.intent.getBooleanExtra("from_login_launcher", false)) {
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    val resultIntent = activity.intent
                    resultIntent.putExtra("name", user.firstName)
                    resultIntent.putExtra("email", user.email)
                    localContext.setResult(Activity.RESULT_OK, resultIntent)
                    localContext.finish()
                    viewModel.logout()
                }) {
                    Text("Save this User to the Launcher")
                }
            }

            //Check if it was opened by Labiba launcher app.
            if(activity.intent.getBooleanExtra("from_portfolio_launcher", false)){
               
                //Mutable states to hold portfolio statistic,\
                var totalCount by remember { mutableStateOf<Int?>(null) }
                var countPrivate by remember { mutableStateOf<Int?>(null) }
                var countPublic by remember { mutableStateOf<Int?>(null) }
                var countLink by remember { mutableStateOf<Int?>(null) }

                val scope = rememberCoroutineScope()
                /**
                 * Launched effect runs once when the user.id changes.
                 * It loads all portfolio statistics.
                 */
                LaunchedEffect(user.id){
                    scope.launch {
                        totalCount = userPortfolio.getPortfolioCount(user.id)
                        countPrivate = userPortfolio.getPortfolioCountByVisibility(user.id, "private")
                        countPublic = userPortfolio.getPortfolioCountByVisibility(user.id, "public")
                        countLink = userPortfolio.getPortfolioCountByVisibility(user.id, "link-only")
                    }
                }



                Spacer(modifier = Modifier.height(15.dp))
                //All data must be fully loaded before enabling the button
                val allLoaded = totalCount != null && countPrivate != null && countPublic != null && countLink != null

                /**
                 * Button sends the result back to the launcher app.
                 * Enabled only after every things has loaded.
                 */
                Button(onClick = {
                    val resultIntent = activity.intent
                    resultIntent.putExtra("result", "Success")
                    resultIntent.putExtra("total", totalCount)
                    resultIntent.putExtra("public", countPublic)
                    resultIntent.putExtra("private", countPrivate)
                    resultIntent.putExtra("link", countLink)
                    localContext.setResult(Activity.RESULT_OK, resultIntent)
                    viewModel.logout()
                    localContext.finish()
                }, enabled = allLoaded
                ){
                    Text("Get Portfolio static!!")
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    viewModel.logout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}