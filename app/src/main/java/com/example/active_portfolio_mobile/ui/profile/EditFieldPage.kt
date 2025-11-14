package com.example.active_portfolio_mobile.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController

/**
 * Screen for editing a single user profile field.
 *
 * @param viewModel ProfileViewModel instance for profile updates.
 * @param field     Name of the field being edited.
 */
@Composable
fun EditFieldPage(
    viewModel : ProfileViewModel,
    field: String,
){
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val navController = LocalNavController.current

    val currentValue = remember (user, field){
        when (field){
            "firstName" -> user?.firstName ?: ""
            "lastName" -> user?.lastName ?: ""
            "username" -> user?.username ?: ""
            "bio" -> user?.bio ?: ""
            "program" -> user?.program ?: ""
            else ->  ""
        }
    }

    var value by rememberSaveable { mutableStateOf(currentValue) }

    val fieldLabel = when (field){
        "firstName" -> "First Name"
        "lastName" -> "Last Name"
        "username" -> "Username"
        "bio" -> "Bio"
        "program" ->  "Program"
        else -> field
    }

    MainLayout {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            if(field == "bio"){
                OutlinedTextField(
                    value= value,
                    onValueChange = { value = it},
                    label = { Text (fieldLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines= 4,
                    maxLines = 6
                )
            } else {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it},
                    label = { fieldLabel},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateField(field, value){
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = value != currentValue // only change can save

            ) {
                Text("Save")
            }
        }
    }
}