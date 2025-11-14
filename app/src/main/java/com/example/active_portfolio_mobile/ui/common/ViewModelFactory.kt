package com.example.active_portfolio_mobile.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel
import com.example.active_portfolio_mobile.ui.profile.ProfileViewModel

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