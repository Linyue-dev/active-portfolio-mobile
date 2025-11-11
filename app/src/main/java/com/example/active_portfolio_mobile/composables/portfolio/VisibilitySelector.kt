package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color


/**
 * A composable that displays a visibility selector for a portfolio.
 * This component shows the current visibility state on an Card.
 * When clicked, it open a dropdown menu with three options: "Private", "Public" and "Link-Only".
 * Selecting an option updates the current visibility and whitened the Box on the words left..
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
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        //Button displaying the current visibility
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ){
            Text("Visibility: ${currentVisibility.replaceFirstChar {it.uppercase()}}",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        //Dropdown menu with visibility options
        if(expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp)
            ) {
                listOf("Private", "Public", "Link-Only").forEach { 
                    option ->
                    val isSelected = option.equals(currentVisibility, ignoreCase = true)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onVisibilityChange(option)
                            }
                            .padding(8.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .border(1.dp, Color.Black)
                                .background(if (isSelected) Color.Black else Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    
                        Text(
                                text = option,
                                color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}