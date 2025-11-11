package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable that display action buttons for creating, saving or deleting
 * a portfolio.
 *
 * When isEditing is false, it show a single Create button.
 * When isEditing is true, it show two button: Save and Delete.
 *
 * @param isEditing True if we are trying to edit a existing portfolio, false if we are
 * trying to create a new Portfolio.
 * @param onSaveClick Callback that is called when the user clicks Save or Create button.
 * @param onDeleteClick Optional callback called when the user clicks the Delete button.
 */
@Composable
fun PortfolioActionButtons(
    isEditing: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: (() -> Unit) ? =  null,
) {

    //Save and Create button.
    Button(
        onClick = onSaveClick,
        modifier = Modifier.fillMaxWidth()
    ){
        Text( if (isEditing) "Save" else "Create")
    }

    //Delete button: if the portfolio doesn't already exist the button will not appear.
    if(isEditing && onDeleteClick != null){
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Delete")
        }
    }
}