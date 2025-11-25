package com.example.active_portfolio_mobile.ui.profile

import android.R.attr.value
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController

/**
 * A reusable page for editing a single user profile field.
 *
 * Supports:
 * - Dynamic field label and initial value
 * - Inline validation for required fields (first name, last name)
 * - Snackbar error handling via ViewModel
 *
 * @param viewModel ProfileViewModel providing the user data and update logic
 * @param field The profile field key (e.g., "firstName", "bio", "username")
 */

@Composable
fun EditFieldPage(
    viewModel : ProfileViewModel,
    field: String,
){
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val user = uiState.user
    val navController = LocalNavController.current

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
    val nameRegex = Regex(  """^[A-Za-zÀ-ÖØ-öø-ÿ\-'\\s]+$""")
    val isNameValid = !isRequired || value.matches(nameRegex)

    MainLayout {
        Box(modifier = Modifier.fillMaxSize()){
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
                        singleLine = true,
                        isError = !isNameValid,
                        supportingText = {
                            if(!isNameValid){
                                Text("Name cannot contain number")
                            }
                        }
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
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}