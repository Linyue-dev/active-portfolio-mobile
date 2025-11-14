package com.example.active_portfolio_mobile.ui.profile

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
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
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import com.example.active_portfolio_mobile.viewModels.UserPortfolio
import kotlinx.coroutines.launch

/**
 * Displays the logged-in user's profile.
 *
 * @param authViewModel Handles logout.
 * @param profileViewModel Provides user profile data.
 * @param onEditProfile Navigates to the edit profile screen.
 */
@Composable
fun ProfilePage(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    onEditProfile: () -> Unit
){
    val localContext = LocalContext.current
    val activity = localContext as ComponentActivity
    val uiState by profileViewModel.uiState.collectAsState()
    val user = uiState.user
    val userPortfolio : UserPortfolio = viewModel()
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
                    /**
                     * Display username only when it is not null.
                     * If null, no Text composable is shown and no space is reserved.
                     */

                    user.username?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                        )
                    }

                    user.bio?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                        )
                    }

                    Text(
                        text = "${user.email}",
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )

                    user.program?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                        )
                    }
                    Text(
                        text = "${user.role}",
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
                    authViewModel.logout()
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
                    authviewModel.logout()
                    localContext.finish()
                }, enabled = allLoaded
                ){
                    Text("Get Portfolio static!!")
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Button(
                    onClick = onEditProfile,
                    modifier = Modifier.weight(1f)
                ){
                    Text("Edit")
                }

                Button(
                    onClick = {
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Portfolio")
                }
                Button(
                    onClick = {
                        authViewModel.logout()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Logout")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
            OutlinedButton(
                onClick = {
                    val resultIntent = Intent().apply {
                        putExtra(
                            "resultMessage",
                            authViewModel.uiState.value.user?.firstName ?: "User"
                        )
                    }
                    activity.setResult(Activity.RESULT_OK, resultIntent)
                    activity.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Return to Launcher")
            }
        }
    }
}