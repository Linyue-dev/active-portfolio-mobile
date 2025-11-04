package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * A composable that displays a visibility selector for a portfolio.
 * This component shows the current visibility state on an OutlinedButton.
 * When clicked, it open a dropdown menu with three options: "Private", "Public" and "Link-Only".
 * Selecting an option updates the current visibility and closes the menu.
 *
 * @param currentVisibility The current selected visibility option.
 * @param onVisibilityChange Callback invoked when the user selects a different visibility.
 */
@Composable
fun VisibilitySelector(
    currentVisibility: String,
    onVisibilityChange: (String) -> Unit
) {
    //Tracks whether the dropdown menu is expanded
    var dropDownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()){
        //Button displaying the current visibility
        OutlinedButton(
            onClick = {dropDownExpanded = true},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Visibility: $currentVisibility")
        }

        //Dropdown menu with visibiluty options
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = {dropDownExpanded = false}
        ) {
            listOf("Private", "Public", "Link-Only").forEach {
                    option ->
                DropdownMenuItem(
                    text = {Text(option)},
                    onClick = {
                        onVisibilityChange(option)
                        dropDownExpanded = false
                    }
                )
            }
        }
    }
}