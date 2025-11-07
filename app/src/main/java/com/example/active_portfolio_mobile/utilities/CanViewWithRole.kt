package com.example.active_portfolio_mobile.utilities

/**
 * A utility function for determining whether a user has the correct role to view something with
 * a certain visibility level. Note that this function assumes the user is not the item's creator.
 * It should therefore be used in conjunction with other checks, especially with items assigned
 * as private.
 * @param userRole The role of the user to be checked.
 * @param visibility The visibility level for the user role to be checked against.
 */
fun canViewWithRole(userRole: String, visibility: String) : Boolean {
    return when (userRole.lowercase()) {
        "admin" -> true
        "cs_community" -> visibility.lowercase() != "private"
        "student" -> visibility.lowercase() !in listOf("private", "cs")
        "teacher" -> visibility.lowercase() !in listOf("private", "cs")
        "public" -> visibility.lowercase() == "public"
        else -> false
    }
}