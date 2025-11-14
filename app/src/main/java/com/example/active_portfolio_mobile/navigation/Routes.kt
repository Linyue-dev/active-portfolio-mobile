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
    object SectionsUpdate: Routes("SectionsUpdateRoute/{adventureId}") {
        fun go(adventureId: String) = "SectionsUpdateRoute/$adventureId"
    }

    // Auth
    object Login : Routes("login")
    object SignUp : Routes("signup")

    // User
    object Profile : Routes("ProfilePageRoute")
    
    //Portfolio
    object CreateUpdatePortfolio : Routes("PortfolioCreateUpdateRoute") {

        fun goCreate(): String =
            "PortfolioCreateUpdateRoute?isEditing=false&portfolioId="

        fun goEdit(portfolioId: String): String =
            "PortfolioCreateUpdateRoute?isEditing=true&portfolioId=$portfolioId"
    }

    object EditProfile : Routes("edit_profile")

    object EditField : Routes("edit_field/{field}") {
        fun routeWithField(field: String) = "edit_field/$field"
    }
}