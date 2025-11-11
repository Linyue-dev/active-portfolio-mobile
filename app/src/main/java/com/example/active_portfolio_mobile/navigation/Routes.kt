package com.example.active_portfolio_mobile.navigation

import com.example.active_portfolio_mobile.data.remote.dto.Portfolio

//Defines the navigation routes used in the app.
//Each object represents a specific screen route for navigation
sealed class Routes(val route: String){
    object Main : Routes("LandingPageRoute")
    object Comment : Routes("CommentPageRoute")
    object About: Routes("AboutUsRoute")
    object AdventureCreate: Routes("AdventureCreateRoute")
    object SectionsUpdate: Routes("SectionsUpdateRoute/{adventureId}") {
        fun go(adventureId: String) = "SectionsUpdateRoute/$adventureId"
    }

    // Auth
    object Login : Routes("LoginPageRoute")
    object SignUp : Routes("SignUpPageRoute")

    // User
    object Profile : Routes("ProfilePageRoute")

    //Portfolio
    object CreateUpdatePortfolio: Routes("PortfolioCreateUpdateRoute"){
        fun goCreate() = "ProfileCreateUpdateRoute?isEditing=false?portfolioID="

        fun goEdit(portfolioId: String) = "ProfileCreateUpdateRoute?isEditing=true?portfolioId=$portfolioId"
    }
}