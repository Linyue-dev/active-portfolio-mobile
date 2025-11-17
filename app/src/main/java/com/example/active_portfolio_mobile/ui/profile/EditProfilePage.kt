package com.example.active_portfolio_mobile.ui.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.active_portfolio_mobile.layouts.MainLayout
import com.example.active_portfolio_mobile.navigation.LocalNavController
import com.example.active_portfolio_mobile.navigation.Routes

/**
 * Displays editable fields of the user profile.
 *
 * @param viewModel Provides and updates profile data.
 * @param onEdit Navigates to the field-specific edit screen.
 */
@Composable
fun EditProfilePage(
    viewModel: ProfileViewModel,
    onEdit : (String) -> Unit
){
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val navController = LocalNavController.current

    if (user == null){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        return
    }

    // Refresh every time enter the page to make sure edit successfully field and display lastest
    LaunchedEffect(Unit) {
        viewModel.getMyProfile()
    }

    MainLayout {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // add rolling
        ) {
            // ===== Profile Picture Section =====

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            ){
                Column (
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    ){
                        AsyncImage(
                            model = user.profilePicture,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton( onClick = {}) {
                        Icon(
                            Icons.Default.CameraAlt,
                            null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Change photo")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ===== Edit Field Section =====
            Text(
                "Personal Information",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            EditableField(
                label = "First Name",
                value = user.firstName
            ){
                onEdit("firstName")
            }

            HorizontalDivider()

            EditableField(
                label = "Last Name",
                value = user.lastName
            ){
                onEdit("lastName")
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Profile Details",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            EditableField(
                label = "Program",
                value = user.program ?: "",
                placeholder = "Add program"
            ){
                onEdit("program")
            }

            HorizontalDivider()

            EditableField(
                label = "Username",
                value = user.username ?: "",
                placeholder = "Add username"
            ){
                onEdit("username")
            }

            HorizontalDivider()

            EditableField(
                label = "Bio",
                value = user.bio ?: "",
                placeholder = "Add bio"

            ){
                onEdit("bio")
            }
            HorizontalDivider()

            // change password
            Text(
                text = "Security",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = { navController.navigate( Routes.ChangePassword.route)},
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Lock, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Change Password")
            }
        }
    }
}

/**
 * A row item representing an editable user profile field.
 *
 * Displays a label on the left and the current value (or a placeholder)
 * on the right. The entire row is clickable and triggers the edit action.
 *
 * @param label The text label for the field (e.g., "First Name").
 * @param value The current value of the field. Empty means no value set.
 * @param placeholder Text to show when `value` is empty. Default is "Add".
 * @param onClick Called when the user taps the row to edit the field.
 */
@Composable
private fun EditableField(
    label : String,
    value: String,
    placeholder: String = "Add",
    onClick: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = label,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.ifEmpty { placeholder },
            style = MaterialTheme.typography.bodyLarge,
            color = if (value.isEmpty()){
                MaterialTheme.colorScheme.primary
            } else{
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}