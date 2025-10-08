package com.example.active_portfolio_mobile.CommentComposable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.ui.theme.DarkPurple
import com.example.active_portfolio_mobile.ui.theme.LightText

/**
 * A composable for entering a simple comment with a text field and a post button.
 *
 * @param commentText The current text inside the input field
 * @param onCommentChange Lambda that is called when the text changes
 * @param onPostClick Lamdba that is called when the button post is clicked
 */
@Composable
fun CommentInput(
    commentText: String,
    onCommentChange : (String) -> Unit,
    onPostClick: () ->Unit
){
    //Horizontal layout for the text and the button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        //Input field for entering a comment
        OutlinedTextField(
            value = commentText,
            onValueChange = onCommentChange,
            label = {Text("Write a comment...")},
            modifier =  Modifier.weight(1f)
                .padding(end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkPurple,
                unfocusedBorderColor = DarkPurple.copy(alpha = 0.6f),
                focusedLabelColor = DarkPurple,
                unfocusedLabelColor = DarkPurple.copy(alpha = 0.6f),
                cursorColor = DarkPurple
            )
        )
        //Button to post the comment
        Button(
            onClick = onPostClick,
            modifier = Modifier.padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkPurple,
                contentColor = LightText
            )
        ){
            Text("Post")
        }
    }
}