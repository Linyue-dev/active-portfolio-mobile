package com.example.active_portfolio_mobile.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.active_portfolio_mobile.Screen.CommentPage
import com.example.active_portfolio_mobile.Screen.AboutUsPage
import com.example.active_portfolio_mobile.Screen.CreateScreen
import com.example.active_portfolio_mobile.Screen.LandingPage
import com.example.active_portfolio_mobile.Screen.adventure.AdventureViewScreen
import com.example.active_portfolio_mobile.Screen.adventure.CreateAdventureScreen
import com.example.active_portfolio_mobile.Screen.adventure.CreateSectionScreen
import com.example.active_portfolio_mobile.Screen.adventure.UpdateSectionsScreen
import com.example.active_portfolio_mobile.ui.profile.ProfilePage
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.ui.common.ViewModelFactory
import com.example.active_portfolio_mobile.ui.auth.LoginPage
import com.example.active_portfolio_mobile.ui.auth.SignUpPage

//Sets up the app navigation using NavHost with three routes: LandingPage,
// CommentPage and AboutUsPage.
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
val LocalAuthViewModel = compositionLocalOf<AuthViewModel> { error("No UserViewModel provided") }
@Composable
fun Router(modifier: Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager =  remember { TokenManager(context) }

    val authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory(tokenManager)
    )
    CompositionLocalProvider(LocalAuthViewModel provides authViewModel) {
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.Main.route,
                modifier = modifier.fillMaxSize()
            ) {
                composable(Routes.Main.route) { LandingPage(modifier) }
                composable(Routes.Comment.route) { CommentPage(modifier) }
                composable(Routes.About.route) { AboutUsPage(modifier) }
                composable(Routes.Create.route) { CreateScreen() }
                // Adventure routes
                composable(Routes.AdventureCreate.route) { CreateAdventureScreen(modifier) }
                composable(Routes.AdventureUpdate.route) {
                    val id = it.arguments?.getString("adventureId")
                    if (id != null) {
                        CreateAdventureScreen(modifier, id)
                    }
                }
                composable(Routes.AdventureView.route) {
                    val id = it.arguments?.getString("adventureId")
                    if (id != null) {
                        AdventureViewScreen(id)
                    }
                }
                composable(Routes.SectionsCreate.route) {
                    val id = it.arguments?.getString("adventureId")
                    if (id != null) {
                        CreateSectionScreen(id)
                    }
                }
                composable(Routes.SectionsUpdate.route) {
                    val id = it.arguments?.getString("adventureId")
                    if (id != null) {
                        UpdateSectionsScreen(id)
                    }
                }

                // Auth
                composable(Routes.Login.route) {
                    LoginPage(
                        authViewModel,
                        onNavigateToSignUp = { navController.navigate(Routes.SignUp.route) },
                        onLoginSuccess = {
                            navController.navigate(Routes.Profile.route) {
                                popUpTo(Routes.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Routes.SignUp.route) {
                    SignUpPage(
                        authViewModel,
                        onNavigateToLogin = { navController.navigate(Routes.Login.route) },
                        onSignUpSuccess = {
                            navController.navigate(Routes.Main.route) {
                                popUpTo(Routes.SignUp.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // profile
                composable(Routes.Profile.route) {
                    ProfilePage(authViewModel)
                }
            }
        }
    }
}