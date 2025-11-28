package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

/**
 * A button for performing a deletion operation, which shows a confirmation pop-up when pressed.
 * @param deleteFunc The function which will handle the deletion operation
 * once the deletion is confirmed.
 */
@Composable
fun DeleteButtonWithConfirm(deleteFunc: () -> Unit) {
    var confirmDelete by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            confirmDelete = true
        }) {
            Text("Delete")
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
                    TextButton(onClick = { deleteFunc() }) {
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