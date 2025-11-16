package com.example.active_portfolio_mobile.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
            "program" -> user?.program ?: ""
            "bio" -> user?.bio ?: ""
            else ->  ""
        }
    }

    var value by rememberSaveable { mutableStateOf(currentValue) }

    val fieldLabel = when (field){
        "firstName" -> "First Name"
        "lastName" -> "Last Name"
        "username" -> "Username"
        "program" ->  "Program"
        "bio" -> "Bio"
        else -> field
    }

    // add flag to make user firstname and lastname cannot be null
    val isRequired = field == "firstName" || field == "lastName"

    MainLayout {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            if(field == "bio"){
                Text(
                    "Tell others about yourself",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if(field == "bio"){
                OutlinedTextField(
                    value= value,
                    onValueChange = { value = it},
                    label = { Text (fieldLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines= 4,
                    maxLines = 6,
                    supportingText = {
                        Text("${value.length} / 500")
                    }
                )
            } else {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it},
                    label = { Text(fieldLabel)},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                // cancel button
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                // save button
                Button(
                    onClick = {
                        if (value != currentValue){
                            viewModel.updateField(field, value){
                                navController.popBackStack()
                            }
                        } else{
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = value != currentValue // only change can save
                            && !uiState.isLoading
                            && !(isRequired && value.isBlank())
                ) {
                    if(uiState.isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save")
                    }
                }
            }
            uiState.error?.let{ error ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}