package com.example.active_portfolio_mobile.composables.adventure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
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
        // ToDo change this to a popup.
        if (confirmDelete) {
            Card {
                Text("Are you sure? Deletion is permanent")
            }
            Row {
                Button(onClick = {
                    deleteFunc()
                }) {
                    Text("Yes, Delete")
                }
                Button(onClick = {
                    confirmDelete = false
                }) {
                    Text("Cancel")
                }
            }
        }
    }
}