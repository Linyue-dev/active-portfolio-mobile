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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel

@Composable
fun ProfilePage(
    viewModel: AuthViewModel
){
    val localContext = LocalContext.current
    val activity = localContext as ComponentActivity
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val user = uiState.user
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