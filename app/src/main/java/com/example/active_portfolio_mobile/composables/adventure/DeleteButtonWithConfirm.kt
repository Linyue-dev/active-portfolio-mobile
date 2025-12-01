package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A button for performing a deletion operation, which shows a confirmation pop-up when pressed.
 * @param deleteFunc The function which will handle the deletion operation
 * once the deletion is confirmed.
 */
@Composable
fun DeleteButtonWithConfirm(deleteFunc: () -> Unit) {
    var confirmDelete by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = {
            confirmDelete = true
        }) {
            Icon(imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Button",
                modifier = Modifier.size(30.dp)
            )
        }
        if(confirmDelete) {
            AlertDialog(
                onDismissRequest = { confirmDelete = false },
                title = {
                    Text(text = "Confirm Deletion")
                },
                text = {
                    Text("Are you sure? Deletion is permanent")
                },
                confirmButton = {
                    TextButton(onClick = {
                        deleteFunc()
                        confirmDelete = false
                    }) {
                        Text("Yes, Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { confirmDelete = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}