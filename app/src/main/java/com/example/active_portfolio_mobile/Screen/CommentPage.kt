package com.example.active_portfolio_mobile.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.active_portfolio_mobile.CommentComposable.Comment
import com.example.active_portfolio_mobile.CommentComposable.CommentInput
import com.example.active_portfolio_mobile.CommentComposable.CommentList
import com.example.active_portfolio_mobile.ui.theme.CommentScreenTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.collections.plus

// Fake initial comments
//This was created by CHATGPT
val initialComments = listOf(
    Comment( "Alice", "This is so cool!", 38, System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)),
    Comment( "Bob", "I love this idea.", 87, System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)),
    Comment(
        "Charlie",
        "Keep up the good work!",
        1000,
        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
    )
)

/**
 * The main screen showing a comment wall with input and list of comments.
 *
 * @param modifier Could be used to customize layout.
 */
@Composable
fun CommentPage(modifier : Modifier = Modifier){
    CommentScreenTheme {

        //Holds the current text input for a new comment
        var commentText by rememberSaveable { mutableStateOf("") }
        //Holds the list of comments displayed in the wall
        var comments by rememberSaveable { mutableStateOf(initialComments) }


        //Tracks the current time so the page is refresh every 60 seconds.
        var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }

        //Update every 60 seconds so the time updates
        LaunchedEffect(Unit) {
            while (true) {
                delay(60_000)
                currentTime = System.currentTimeMillis()
            }
        }

        //Veritcal layout for input and list
        CommentScreenTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                CommentInput(
                    commentText = commentText,
                    onCommentChange = { commentText = it },
                    onPostClick = {
                        if (commentText.isNotBlank()) {
                            comments = comments + Comment("Guest", commentText, 0)
                            commentText = ""
                        }
                    }
                )
                Spacer(modifier = Modifier.height(6.dp))
                CommentList(comments, currentTime, Modifier.fillMaxSize())
            }
        }
    }
}