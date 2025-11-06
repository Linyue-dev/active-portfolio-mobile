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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout
/**
 * Sign up page for new user registration.
 *
 * Displays a form with fields for email, first name, last name, program, password,
 * and password confirmation. Validates inputs client-side before submitting to the backend.
 *
 * @param viewModel AuthViewModel that handles signup logic and authentication state
 * @param onNavigateToLogin Callback to navigate to the login page
 * @param onSignUpSuccess Callback invoked when signup is successful
 */
@Composable
fun SignUpPage(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.isLoggedIn) {
        if(uiState.isLoggedIn){
            onSignUpSuccess()
        }
    }

    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }

    var firstName by rememberSaveable { mutableStateOf("") }
    var firstNameError by rememberSaveable { mutableStateOf<String?>(null) }

    var lastName by rememberSaveable { mutableStateOf("") }
    var lastNameError by rememberSaveable { mutableStateOf<String?>(null) }

    var program by rememberSaveable { mutableStateOf("") }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    MainLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create Account")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = {Text ("Email")},
                singleLine = true,
                enabled = !uiState.isLoading,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null){
                        Text(emailError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = null
                },
                label = {Text ("First Name")},
                singleLine = true,
                enabled = !uiState.isLoading,
                isError = firstNameError != null,
                supportingText = {
                    if (firstNameError != null){
                        Text(firstNameError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = null
                },
                label = {Text ("Last Name")},
                singleLine = true,
                enabled = !uiState.isLoading,
                isError = lastNameError != null,
                supportingText = {
                    if (lastNameError != null){
                        Text(lastNameError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()

            )

            OutlinedTextField(
                value = program,
                onValueChange = {
                    program = it
                },
                label = {Text ("Program")},
                singleLine = true,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = {Text ("Password")},
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !uiState.isLoading,
                isError = passwordError != null,
                supportingText = {
                    if(passwordError != null){
                        Text(passwordError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = {Text ("ConfirmPassword")},
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !uiState.isLoading,
                isError = confirmPasswordError != null,
                supportingText = {
                    if(confirmPasswordError != null){
                        Text(confirmPasswordError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display API error
            if (uiState.error != null){
                Text(
                    text = uiState.error!!
                )
            }
            Button(
                onClick = {
                    // Clear all errors first
                    emailError = null
                    firstNameError = null
                    lastNameError = null
                    passwordError = null
                    confirmPasswordError = null

                    // Validate each field
                    var hasError = false

                    if (email.isBlank()){
                        emailError = "Email required"
                        hasError = true
                    }

                    if (firstName.isBlank()){
                        firstNameError = "First Name required"
                        hasError = true
                    }
                    if (lastName.isBlank()){
                        lastNameError = "Last Name required"
                        hasError = true
                    }

                    if (password.length < 6){
                        passwordError = "Password must be at least 6 characters"
                        hasError = true
                    }
                    if (confirmPassword != password){
                        passwordError = "Passwords do not match"
                        hasError = true
                    }

                    if(!hasError){
                        viewModel.signup(firstName,lastName,email,program,password)
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if(uiState.isLoading){
                    CircularProgressIndicator(
                        Modifier.size(20.dp)
                    )
                } else{
                    Text("Sign Up")
                }
            }
            TextButton(onClick = onNavigateToLogin){
                Text("Already have an account? Log In")
            }
        }
    }
}