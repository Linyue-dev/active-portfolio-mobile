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
    object Login : Routes("login")
    object SignUp : Routes("signup")


    //Portfolio
    object CreateUpdatePortfolio : Routes(
        "PortfolioCreateUpdateRoute?isEditing={isEditing}&portfolioId={portfolioId}"
    ) {
        fun goCreate() =
            "PortfolioCreateUpdateRoute?isEditing=false&portfolioId="

        fun goEdit(portfolioId: String) = "ProfileCreateUpdateRoute?isEditing=true?portfolioId=$portfolioId"
    }

    // User Profile
    object Profile : Routes("ProfilePageRoute")
    object EditProfile : Routes("edit_profile")
    object EditField : Routes("edit_field/{field}") {
        fun go(field: String) = "edit_field/$field"
    }
    object ChangePassword : Routes("change_password")

    // Search
    object SearchUser : Routes("search?q={query}"){
        fun go(query: String) = "search?q=$query"
    }

    // Check other user profile
    object OtherUserProfile : Routes("user/{username}"){
        fun go(username: String) = "user/$username"
    }
}