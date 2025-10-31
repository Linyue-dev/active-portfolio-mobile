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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes

@Composable
fun LoginPage(
    viewModel: AuthViewModel,
){
    MainLayout {
        val navController = LocalNavController.current
        val uiState by viewModel.uiState.collectAsState()

        // add
        LaunchedEffect(uiState.isLoggedIn) {
            if (uiState.isLoggedIn) {
                navController.navigate(Routes.Profile.route) {
                    popUpTo(Routes.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        var email by  rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

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
                    if (uiState.error != null) viewModel.cleanError()
                },
                label = {Text ("Email")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (uiState.error != null) viewModel.cleanError()
                },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text ("Password")},
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.login(email, password)},
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
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean = false,
    error: String? = null,
    onLoginClick: () -> Unit = {}
){

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
            onValueChange = onEmailChange,
            label = {Text ("Email")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            label = { Text ("Password")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Sign In")
        }

        TextButton({}) {
            Text("Don't have an account? Sign up")
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        email = "test@example.com",
        password = "123456",
        onEmailChange = {},
        onPasswordChange =  {},
        isLoading = false,
        error = null
    )
}