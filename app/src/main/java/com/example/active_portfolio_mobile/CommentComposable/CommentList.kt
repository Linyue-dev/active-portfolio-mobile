package com.example.active_portfolio_mobile.CommentComposable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.reversed


/**
 * Display a scrollable list of comments in reverse chronological order
 *
 * @param comments The list of comment to show
 * @param currentTime The current time in milliseconds
 * @param modifier Modifier to customize layout and more
 */
@Composable
fun CommentList(comments: List<Comment>, currentTime: Long, modifier: Modifier){
    LazyColumn(modifier = modifier
        .fillMaxSize()) {
        items(comments.reversed()){
            comment ->
            CommentCard(comment, currentTime)
        }
    }
}