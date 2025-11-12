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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
/**
 * Displays the logged-in user's profile.
 *
 * @param authViewModel Handles logout.
 * @param profileViewModel Provides user profile data.
 * @param onEditProfile Navigates to the edit profile screen.
 */
@Composable
fun ProfilePage(
    authviewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    onEditProfile: () -> Unit
){
    val localContext = LocalContext.current
    val activity = localContext as ComponentActivity
    val navController = LocalNavController.current
    val uiState by profileViewModel.uiState.collectAsState()
    val user = uiState.user

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
                    authviewModel.logout()
                }) {
                    Text("Save this User to the Launcher")
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
                        authviewModel.logout()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}