package com.example.active_portfolio_mobile.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.ui.profile.ProfileViewModel
/**
 * Factory for creating ViewModel instances with dependencies.
 *
 * This factory is responsible for instantiating ViewModels that require
 * constructor parameters, specifically those that need TokenManager for
 * authentication and API calls.
 *
 * @param tokenManager TokenManager instance for managing JWT tokens and user sessions
 * @throws IllegalArgumentException if an unknown ViewModel class is requested
 *
 * Example usage:
 * ```
 * val factory = ViewModelFactory(tokenManager)
 * val authViewModel: AuthViewModel = viewModel(factory = factory)
 * ```
 */
class ViewModelFactory (
    private  val tokenManager: TokenManager
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create (modelClass: Class<T>) : T{

        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(tokenManager) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(tokenManager) as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}