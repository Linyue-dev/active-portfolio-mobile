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
import com.example.active_portfolio_mobile.Screen.LandingPage
import com.example.active_portfolio_mobile.ui.profile.ProfilePage
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.ui.common.ViewModelFactory
import com.example.active_portfolio_mobile.ui.auth.LoginPage

//Sets up the app navigation using NavHost with three routes: LandingPage,
// CommentPage and AboutUsPage.
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
@Composable
fun Router(modifier: Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager =  remember { TokenManager(context) }

    val authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory(tokenManager)
    )

    CompositionLocalProvider(
        LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = Routes.Main.route, modifier = modifier.fillMaxSize()) {
            composable(Routes.Main.route) { LandingPage(modifier)}
            composable(Routes.Comment.route) {CommentPage(modifier)}
            composable(Routes.About.route) {AboutUsPage(modifier)}

            // Auth
            composable(Routes.Login.route) {
                LoginPage(authViewModel)
            }

            // profile
            composable(Routes.Profile.route) {
                ProfilePage(authViewModel)
            }
        }
    }
}