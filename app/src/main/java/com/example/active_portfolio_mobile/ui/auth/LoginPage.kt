package com.example.active_portfolio_mobile.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes
/**
 * Login page for existing user authentication.
 *
 * Displays email and password input fields. Validates credentials and navigates
 * to the profile page upon successful login.
 *
 * @param viewModel AuthViewModel that handles login logic and authentication state
 * @param onNavigateToSignUp Callback to navigate to the sign up page
 * @param onLoginSuccess Callback invoked when login is successful
 */
@Composable
fun LoginPage(
    startingEmail: String = "",
    viewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
){
    MainLayout {
        val uiState by viewModel.uiState.collectAsState()

        // add
        LaunchedEffect(uiState.isLoggedIn) {
            if (uiState.isLoggedIn) {
                onLoginSuccess()
            }
        }


//        var email by  rememberSaveable { mutableStateOf("") }
        var emailError by rememberSaveable { mutableStateOf<String?>(null) }

        var email by  rememberSaveable { mutableStateOf(startingEmail) }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            Text("Sign In")

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    viewModel.cleanError()
                },
                label = {Text ("Email")},
                enabled = !uiState.isLoading,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null){
                        Text(
                            text = emailError!!,
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                    viewModel.cleanError()
                },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text ("Password")},
                enabled = !uiState.isLoading,
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null){
                        Text(passwordError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    emailError = null
                    passwordError = null

                    var hasError = false
                    if (email.isBlank()){
                        emailError = "Email required"
                        hasError = true
                    }

                    if (password.isBlank()){
                        passwordError = "Password required"
                        hasError = true
                    }

                    if(!hasError){
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading){
                    CircularProgressIndicator(Modifier.size(20.dp))
                } else {
                    Text ("Sign In")
                }
            }

            if (uiState.error != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = onNavigateToSignUp) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}
