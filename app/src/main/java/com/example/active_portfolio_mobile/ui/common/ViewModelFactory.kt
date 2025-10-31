package com.example.active_portfolio_mobile.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.active_portfolio_mobile.data.local.TokenManager
import com.example.active_portfolio_mobile.ui.auth.AuthViewModel

class ViewModelFactory (
    private  val tokenManager: TokenManager
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create (modelClass: Class<T>) : T{
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}