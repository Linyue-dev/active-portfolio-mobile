package com.example.active_portfolio_mobile.composables.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.ui.theme.DarkPurple


/**
 * A composable function that display a single comment in a card format.
 *
 * @param comment The comment object containing the author, message, like and time
 * @param currentTime The current time in milliseconds used to calculate "time ago"
 */
@Composable
fun CommentCard(comment: Comment, currentTime: Long){
    //Card composable
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(16.dp)
    ){
        //Column to stack text elements veritcally
        Column(modifier = Modifier.padding(8.dp)){
            Text(comment.author, style = MaterialTheme.typography.labelLarge, color = DarkPurple)
            Text(comment.message, style = MaterialTheme.typography.bodyLarge)
            
            Row( 
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("\uD83D\uDC4D ${comment.likes}")
                Text("\uD83D\uDD52 ${comment.timestamp.timeAgo(currentTime)}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}