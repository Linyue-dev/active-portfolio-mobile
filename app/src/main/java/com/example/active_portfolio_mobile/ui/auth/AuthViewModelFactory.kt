package com.example.active_portfolio_mobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.active_portfolio_mobile.data.local.TokenManager

class AuthViewModelFactory (
    private  val tokenManager: TokenManager
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create (modelClass: Class<T>) : T{
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel( tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}