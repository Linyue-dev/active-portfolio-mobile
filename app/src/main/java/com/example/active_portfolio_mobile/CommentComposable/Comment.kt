package com.example.active_portfolio_mobile.CommentComposable

import java.util.concurrent.TimeUnit

/**
 * Represent a user comment with the author, message, likes, and timestamp.
 *
 * @property author The name of the comment author.
 * @property message The content of the comment.
 * @property likes The number of likes the comment has.
 * @property timestamp The time the comment was posted.
 */
data class Comment(val author: String, val message: String, val likes: Int, val timestamp: Long = System.currentTimeMillis())

/**
 * Function to represent a timestamp in long to calculate a "time ago".
 *
 * @param now The current time in milliseconds.
 * @return A string representing how long the comment was posted.
 */
fun Long.timeAgo(now: Long = System.currentTimeMillis()): String{
    val diff = now - this
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        minutes < 1 -> "Just now"
        minutes == 1L -> "1 min ago"
        minutes < 60 -> "$minutes mins ago"
        hours ==  1L -> "1 hour ago"
        hours <24 -> "$hours hrs ago"
        days == 1L -> "Yesterday"
        else -> "$days days ago"
    }
}