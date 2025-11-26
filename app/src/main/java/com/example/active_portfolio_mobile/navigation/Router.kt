package com.example.active_portfolio_mobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navArgument
import com.example.active_portfolio_mobile.Screen.CommentPage
import com.example.active_portfolio_mobile.Screen.AboutUsPage
import com.example.active_portfolio_mobile.Screen.CreateOrEditPortfolioScreen
import com.example.active_portfolio_mobile.Screen.CreateScreen
import com.example.active_portfolio_mobile.Screen.InformationPage
import com.example.active_portfolio_mobile.Screen.LandingPage
import com.example.active_portfolio_mobile.Screen.UserPortfolioPage
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
import com.example.active_portfolio_mobile.ui.profile.ChangePasswordPage
import com.example.active_portfolio_mobile.ui.profile.EditFieldPage
import com.example.active_portfolio_mobile.ui.profile.EditProfilePage
import com.example.active_portfolio_mobile.ui.profile.ProfileViewModel
import com.example.active_portfolio_mobile.ui.search.SearchResultPage
import com.example.active_portfolio_mobile.ui.search.SearchViewModel
import com.example.active_portfolio_mobile.viewModels.PortfoliosVM

//Sets up the app navigation using NavHost with three routes: LandingPage,
// CommentPage and AboutUsPage.
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
val LocalAuthViewModel = compositionLocalOf<AuthViewModel> { error("No UserViewModel provided") }
@Composable
fun Router(modifier: Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager =  remember { TokenManager(context) }
    val getPortfolio: PortfoliosVM = viewModel()
    val searchViewModel: SearchViewModel = viewModel()

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
                composable(Routes.Info.route) { InformationPage(modifier) }
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
                        viewModel = authViewModel,
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
                        viewModel = authViewModel,
                        onNavigateToLogin = { navController.navigate(Routes.Login.route) },
                        onSignUpSuccess = {
                            navController.navigate(Routes.Main.route) {
                                popUpTo(Routes.SignUp.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                // Deep link for log-in launcher
                composable(
                    "sign-in?email={email}",
                    deepLinks = listOf(navDeepLink {
                        uriPattern = "project://ap.sign-in/?email={email}"
                    })
                ) { backStackEntry ->
                    LoginPage(
                        startingEmail = backStackEntry.arguments?.getString("email") ?: "",
                        viewModel = authViewModel,
                        onNavigateToSignUp = { navController.navigate(Routes.SignUp.route) },
                        onLoginSuccess = {
                            navController.navigate(Routes.Profile.route) {
                                popUpTo(Routes.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                
                //Portfolio
                composable(
                    Routes.CreateUpdatePortfolio.route,
                    arguments = listOf(
                        navArgument("isEditing") {
                            type = NavType.BoolType
                            defaultValue = false
                        },
                        navArgument("portfolioId") {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )) { backStackEntry ->
                    val isEditing = backStackEntry.arguments?.getBoolean("isEditing") ?: false
                    val portfolioId = backStackEntry.arguments?.getString("portfolioId") ?: ""

                    val existingPortfolio =
                        if (isEditing) {
                            LaunchedEffect(Unit) {
                                getPortfolio.loadOnePortfolio(portfolioId)
                            }
                            val portfolioState = getPortfolio.portfolio.collectAsStateWithLifecycle()

                            portfolioState.value
                        } else null
                    CreateOrEditPortfolioScreen(
                        isEditing = isEditing,
                        existingPortfolio = existingPortfolio
                    )
                }

                /**
                 * Profile
                 */
                composable(Routes.Profile.route) {
                    val isLoggedIn = authViewModel.uiState.collectAsState().value.isLoggedIn
                    LaunchedEffect(isLoggedIn) {
                        if (!isLoggedIn) {
                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.SignUp.route) { inclusive = true }
                            }
                        }
                    }

                    if (isLoggedIn) {
                        val profileViewModel: ProfileViewModel = viewModel(
                            factory = ViewModelFactory(tokenManager)
                        )
                        ProfilePage(
                            username = null,
                            authViewModel,
                            profileViewModel,
                            onEditProfile = {
                                navController.navigate(Routes.EditProfile.route)
                            }
                        )
                    }
                }
                composable(Routes.EditProfile.route) {
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ViewModelFactory(tokenManager)
                    )
                    EditProfilePage(
                        viewModel = profileViewModel,
                        onEdit = { field ->
                            navController.navigate(Routes.EditField.go(field))
                        }
                    )
                }

                composable(
                    route = Routes.EditField.route,
                    arguments = listOf(
                        navArgument("field") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val field = backStackEntry.arguments?.getString("field") ?: ""
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ViewModelFactory(tokenManager)
                    )
                    EditFieldPage(
                        viewModel = profileViewModel,
                        field = field
                    )
                }

                composable (
                    route = Routes.ChangePassword.route
                ){
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ViewModelFactory(tokenManager)
                    )
                    ChangePasswordPage(
                        viewModel = profileViewModel
                    )
                }

                /**
                 * Search user
                 */
                composable(
                    route = Routes.SearchUser.route,
                    arguments = listOf(
                        navArgument("query"){
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) { backStackEntry  ->
                    val query = backStackEntry.arguments?.getString("query") ?: ""
                    SearchResultPage(query = query, viewModel = searchViewModel)

                }
                composable(
                    route = Routes.OtherUserProfile.route,
                    arguments = listOf(
                        navArgument("username"){
                            type= NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ViewModelFactory(tokenManager)
                    )
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    ProfilePage(
                        username = username,
                        authViewModel = authViewModel,
                        profileViewModel = profileViewModel,
                        onEditProfile = {}
                    )
                }

                /**
                 * Portfolio by user
                 */
                composable(
                    route = Routes.UserPortfolio.route,
                    arguments = listOf(
                        navArgument("userId"){
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: ""
                    UserPortfolioPage(userId)
                }
            }
        }
    }
}