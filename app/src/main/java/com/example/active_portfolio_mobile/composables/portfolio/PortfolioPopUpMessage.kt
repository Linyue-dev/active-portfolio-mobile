package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
* A reusable popup dialog component that displays a simple message to the user.
* It appears only when a non-null popupMessage is provided.
* 
* @param popupMessage The message text to display inside the popup. If null,
 * the popup will not show.
* @param onDismiss Callback function invoked when the user clicks "Ok" 
 * or dismisses the dialog.
*/
@Composable
fun PopUpMessage(popupMessage: String?, onDismiss: () -> Unit){
    //Only display the popup if a message is provided
    if(popupMessage !=null){
        AlertDialog(
            //Called when the user taps outside the dialog or presses back
            onDismissRequest = onDismiss,

            //Text displayed
            title = {Text("Notification")},
            text = {Text(popupMessage)},
            //Confirmation button section
            confirmButton = {
                Button(onClick = onDismiss){
                    Text("Ok")
                }
            }
        )
    }
}