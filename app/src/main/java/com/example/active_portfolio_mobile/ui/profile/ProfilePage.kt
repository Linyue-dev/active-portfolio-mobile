package com.example.active_portfolio_mobile.ui.profile

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
//import com.example.active_portfolio_mobile.viewModels.UserPortfolio

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
    val authState by authViewModel.uiState.collectAsState()
    val user = uiState.user
//    val userPortfolio : UserPortfolio = viewModel()

    // Listen for changes in the logged-in user's email and reload the profile
    LaunchedEffect(authState.user?.email) {
        if (authState.user != null){
            profileViewModel.getMyProfile()
        }
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
                .verticalScroll(rememberScrollState())
        ) {
            // ===== Banner + Profile Picture =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ){
                if (user.banner.isNullOrEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            )
                    )
                } else {

                    // Banner
                    AsyncImage(
                        model = user.banner,
                        contentDescription = "Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // image
                ProfilePicture(
                    imageUrl = user.profilePicture,
                    firstName = user.firstName,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = 16.dp, y = 60.dp)
                        .border(
                            4.dp,
                            MaterialTheme.colorScheme.surface,
                            CircleShape
                        )
                )
            }
            Spacer(modifier = Modifier.height(70.dp))

            // ===== Name + Username + Role =====
            Column (
                modifier = Modifier.padding(20.dp)
            ){
                // name
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Username + Role
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    user.username?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = "@$it",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface (
                        shape = RoundedCornerShape(12.dp),
                        color = when (user.role){
                            "teacher" -> MaterialTheme.colorScheme.primaryContainer
                            "student" -> MaterialTheme.colorScheme.secondaryContainer
                            "public" -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    ) {
                        Text(
                            text = user.role,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ===== Email =====
                InfoRow(
                    icon = Icons.Default.Email,
                    text = user.email
                )

                // ===== Program =====
                user.program?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(
                        icon = Icons.Default.School,
                        text = it
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // ===== Bio =====
                user.bio?.takeIf { it.isNotBlank() }?.let {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
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
            // ===== Button =====

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
//                LaunchedEffect(user.id){
//                    scope.launch {
//                        totalCount = userPortfolio.getPortfolioCount(user.id)
//                        countPrivate = userPortfolio.getPortfolioCountByVisibility(user.id, "private")
//                        countPublic = userPortfolio.getPortfolioCountByVisibility(user.id, "public")
//                        countLink = userPortfolio.getPortfolioCountByVisibility(user.id, "link-only")
//                    }
//                }

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
                    authViewModel.logout()
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                OutlinedButton(
                    onClick = onEditProfile,
                    modifier = Modifier.weight(1f)
                ){
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = {
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Work, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Portfolio", maxLines = 1)
                }
                OutlinedButton(
                    onClick = {
                        authViewModel.logout()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Logout")
                }
            }
        }
    }
}

/**
 * Displays a circular profile picture. If no image URL is provided,
 * it shows the first letter of the user's first name instead.
 *
 * @param imageUrl URL of the profile image. If null or empty, a placeholder is shown.
 * @param firstName User's first name, used to generate placeholder text.
 * @param modifier Modifier applied to the image or placeholder container.
 */
@Composable
private fun ProfilePicture(
    imageUrl: String?,
    firstName: String,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isNullOrEmpty()) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = firstName.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Profile picture",
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * A row that displays an icon followed by text, commonly used for contact
 * or profile information entries.
 *
 * @param icon Icon shown on the left side.
 * @param text The text displayed next to the icon.
 */
@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}