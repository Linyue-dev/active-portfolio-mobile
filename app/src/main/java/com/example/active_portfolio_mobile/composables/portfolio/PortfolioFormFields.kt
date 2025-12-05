package com.example.active_portfolio_mobile.composables.portfolio

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable that displays from fields for a portfolio's title
 * and description.
 *
 * @param title The current value of the portfolio title field.
 * @param onTitleChange Callback called when the title text change.
 * @param description The current value of the portfolio description field.
 * @param onDescriptionChange Callback called when the description text change.
 * @param titleError The error message if the title is empty.
 */
@Composable
fun PortfolioFormFields(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    titleError: String? = null
) {
    //Title input field
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        isError = titleError != null,
        label = {Text("Title")},
        placeholder  = {Text("Enter portfolio title...")},
        //Give a little red text under the title box if the title is empty
        supportingText = {
            if(titleError != null){
                Text(
                    text = "Title is required.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    //Description input field
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = {Text("Description")},
        placeholder  = {Text("Enter portfolio description...")},
        modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp)        //Ensure the description field has a minimum height.
    )
    Spacer(modifier = Modifier.height(12.dp))

}