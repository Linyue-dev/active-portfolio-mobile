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
import androidx.compose.material.icons.filled.AlternateEmail
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
import androidx.compose.ui.unit.sp
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
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate to profile when login succeeds
    LaunchedEffect(uiState.isLoggedIn) {
        if(uiState.isLoggedIn){
            onSignUpSuccess()
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

    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }

    var firstName by rememberSaveable { mutableStateOf("") }
    var firstNameError by rememberSaveable { mutableStateOf<String?>(null) }

    var lastName by rememberSaveable { mutableStateOf("") }
    var lastNameError by rememberSaveable { mutableStateOf<String?>(null) }

    var program by rememberSaveable { mutableStateOf("") }

    var username by rememberSaveable { mutableStateOf("") }
    var usernameError by rememberSaveable { mutableStateOf<String?>(null)}

    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordVisible by remember() {  mutableStateOf(false)}

    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordVisible by remember() {  mutableStateOf(false)}

    MainLayout {
        Box( modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                        viewModel.cleanError()
                    },
                    label = { Text("Email", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
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

                // First time
                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        firstNameError = null
                        viewModel.cleanError()
                    },
                    label = {Text ("First Name",fontSize = 14.sp)},
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

                // Last Name
                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        lastNameError = null
                        viewModel.cleanError()
                    },
                    label = {Text ("Last Name",fontSize = 14.sp)},
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

                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = null
                        viewModel.cleanError()
                    },
                    label = {Text ("Username",fontSize = 14.sp)},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AlternateEmail,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    isError = usernameError != null,
                    supportingText = {
                        if (usernameError != null){
                            Text(usernameError!!)
                        } else{
                            Text("3-20 characters, letters and numbers")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Program
                OutlinedTextField(
                    value = program,
                    onValueChange = {
                        program = it
                        viewModel.cleanError()
                    },
                    label = {Text ("Program",fontSize = 14.sp)},
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                        viewModel.cleanError()
                    },
                    visualTransformation =  if (passwordVisible)
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
                    label = {Text ("Password",fontSize = 14.sp)},
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    isError = passwordError != null,
                    supportingText = {
                        if(passwordError != null){
                            Text(passwordError!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                        viewModel.cleanError()
                    },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible)
                                    "Hide password"
                                else
                                    "Show password"
                            )
                        }
                    },
                    label = {Text ("ConfirmPassword",fontSize = 14.sp)},
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    isError = confirmPasswordError != null,
                    supportingText = {
                        if(confirmPasswordError != null){
                            Text(confirmPasswordError!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sign Up Button
                Button(
                    onClick = {
                        // Clear all errors first
                        emailError = null
                        firstNameError = null
                        lastNameError = null
                        usernameError = null
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

                        if (username.isBlank()){
                            usernameError = "Username Name required"
                            hasError = true
                        }

                        if (password.length < 6){
                            passwordError = "Password must be at least 6 characters"
                            hasError = true
                        }

                        if (confirmPassword != password){
                            confirmPasswordError = "Passwords do not match"
                            hasError = true
                        }

                        if(!hasError){
                            viewModel.signup(firstName,lastName,email,program,password, username)
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
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}