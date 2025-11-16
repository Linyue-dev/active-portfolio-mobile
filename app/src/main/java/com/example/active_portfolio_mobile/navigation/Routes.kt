package com.example.active_portfolio_mobile.navigation

//Defines the navigation routes used in the app.
//Each object represents a specific screen route for navigation
sealed class Routes(val route: String){
    object Main : Routes("LandingPageRoute")
    object Comment : Routes("CommentPageRoute")
    object About: Routes("AboutUsRoute")

    object Create: Routes("CreateScreenRoute")

    // Adventures
    object AdventureCreate: Routes("AdventureCreateRoute")
    object AdventureUpdate: Routes("AdventureUpdateRoute/{adventureId}") {
        fun go(adventureId: String) = "AdventureUpdateRoute/$adventureId"
    }
    object AdventureView: Routes("AdventureViewRoute/{adventureId}") {
        fun go(adventureId: String) = "AdventureViewRoute/$adventureId"
    }
    object SectionsCreate: Routes("SectionsCreateRoute/{adventureId}") {
        fun go(adventureId: String) = "SectionsCreateRoute/$adventureId"
    }
    object SectionsUpdate: Routes("SectionsUpdateRoute/{adventureId}") {
        fun go(adventureId: String) = "SectionsUpdateRoute/$adventureId"
    }

    // Auth
    object Login : Routes("LoginPageRoute")
    object SignUp : Routes("SignUpPageRoute")

    // User
    object Profile : Routes("ProfilePageRoute")
}