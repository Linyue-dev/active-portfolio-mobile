package com.example.active_portfolio_mobile.Navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.active_portfolio_mobile.Screen.CommentPage
import com.example.active_portfolio_mobile.Screen.AboutUsPage
import com.example.active_portfolio_mobile.Screen.LandingPage
import com.example.active_portfolio_mobile.Screen.adventure.CreateAdventureScreen

//Sets up the app navigation using NavHost with three routes: LandingPage,
// CommentPage and AboutUsPage.
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
@Composable
fun Router(modifier: Modifier) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = Routes.Main.route, modifier = modifier.fillMaxSize()) {
            composable(Routes.Main.route) { LandingPage(modifier)}
            composable(Routes.Comment.route) {CommentPage(modifier)}
            composable(Routes.About.route) {AboutUsPage(modifier)}
            // Adventure routes
            composable(Routes.AdventureCreate.route) { CreateAdventureScreen(modifier) }
        }
    }
}