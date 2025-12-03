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
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.active_portfolio_mobile.composables.adventure.AdventureNavigationList
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.viewModels.UserPortfolio
import kotlinx.coroutines.launch

/**
 * Displays the currently authenticated user's profile page.
 *
 * This screen shows:
 * - Banner and profile picture
 * - User's name, username, email, program, and bio
 * - Buttons for editing profile, viewing portfolio, and logging out
 * - Optional return-data buttons when launched from external "launcher" apps
 *
 * Reactive Behavior:
 * - Automatically refreshes the profile whenever the logged-in user's email changes
 * - Shows a loading indicator while the profile is being fetched
 *
 * @param authViewModel Handles authentication actions such as logout.
 * @param profileViewModel Provides user profile data and refresh logic.
 * @param onEditProfile Callback invoked when user taps the “Edit” button.
 */
@Composable
fun ProfilePage(
    username: String? = null,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    onEditProfile: () -> Unit
){
    val localContext = LocalContext.current
    val navController = LocalNavController.current
    val activity = localContext as ComponentActivity
    val uiState by profileViewModel.uiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    val userPortfolio : UserPortfolio = viewModel()
    val isOwnProfile = username == null || username == authState.user?.username


    // Listen for changes in the logged-in user's email and reload the profile
    LaunchedEffect(username,authState.user?.email) {
        if (username == null ){
            if (authState.user != null){
                profileViewModel.getMyProfile()
            }
        } else {
            profileViewModel.loadOtherUserProfile(username)
       }
    }

    if(uiState.user == null){
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        return
    }
    val user = uiState.user!!

    MainLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // ===== Banner + Profile Picture =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                if (user.banner.isNullOrEmpty()) {
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
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
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
                ) {
                    user.username?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = "@$it",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (user.role) {
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

                // ===== Bio =====
                user.bio?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                }
            }

            // ===== Button =====
            if (isOwnProfile) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onEditProfile,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("Edit",fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Routes.UserPortfolio.go(user.id))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Dashboard, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("Portfolio", maxLines = 1,fontSize = 12.sp)
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
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text("Logout",fontSize = 12.sp)
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
                if (activity.intent.getBooleanExtra("from_portfolio_launcher", false)) {

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
                    LaunchedEffect(user.id) {
                        scope.launch {
                            totalCount = userPortfolio.getPortfolioCount(user.id)
                            countPrivate =
                                userPortfolio.getPortfolioCountByVisibility(user.id, "private")
                            countPublic =
                                userPortfolio.getPortfolioCountByVisibility(user.id, "public")
                            countLink =
                                userPortfolio.getPortfolioCountByVisibility(user.id, "link-only")
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                    //All data must be fully loaded before enabling the button
                    val allLoaded =
                        totalCount != null && countPrivate != null && countPublic != null && countLink != null

                    /**
                     * Button sends the result back to the launcher app.
                     * Enabled only after every things has loaded.
                     */
                    Button(
                        onClick = {
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
                    ) {
                        Text("Get Portfolio static!!")
                    }
                }
            }
            // ===== Adventure List =====
            AdventureNavigationList(user.id)
        }
    }
}

/**
 * Renders a circular profile picture.
 *
 * If [imageUrl] is provided, displays the image.
 * Otherwise, shows a circular placeholder containing the capitalized first letter of the user's first name.
 *
 * @param imageUrl URL of the profile picture, or null/empty for placeholder.
 * @param firstName Used to generate placeholder text when there is no image.
 * @param modifier Optional [Modifier] applied to the outer image container.
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