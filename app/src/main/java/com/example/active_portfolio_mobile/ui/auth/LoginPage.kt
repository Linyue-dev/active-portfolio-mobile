package com.example.active_portfolio_mobile.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout

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
    val snackbarHostState = remember { SnackbarHostState()}
    val uiState by viewModel.uiState.collectAsState()
    var email by  rememberSaveable { mutableStateOf(startingEmail) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordVisible by remember() {  mutableStateOf(false)}

    // Navigate to profile when login succeeds
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }
    // display error messages via Snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let{ error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Long
            )
            viewModel.cleanError()
        }
    }
    MainLayout {
        Box(modifier = Modifier.fillMaxSize()){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                        viewModel.cleanError()
                    },
                    label = {Text ("Email")},
                    leadingIcon ={
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
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

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                        viewModel.cleanError()
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible)
                                    "Hide password"
                                else
                                    "Show password"
                            )
                        }
                    },
                    label = { Text ("Password")},
                    enabled = !uiState.isLoading,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
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

                TextButton(onClick = onNavigateToSignUp) {
                    Text("Don't have an account? Sign up")
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
