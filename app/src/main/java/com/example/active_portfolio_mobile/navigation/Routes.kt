package com.example.active_portfolio_mobile.navigation

//Defines the navigation routes used in the app.
//Each object represents a specific screen route for navigation
sealed class Routes(val route: String){
    object Main : Routes("LandingPageRoute")
    object Comment : Routes("CommentPageRoute")
    object About: Routes("AboutUsRoute")
    object AdventureCreate: Routes("AdventureCreateRoute")

    // Auth
    object Login : Routes("LoginPageRoute")

    // User
    object Profile : Routes("ProfilePageRoute")
}