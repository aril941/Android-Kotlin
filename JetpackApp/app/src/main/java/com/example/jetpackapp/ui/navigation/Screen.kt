package com.example.jetpackapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object Profile : Screen("profile")
    object Detail : Screen("home/{detailId}") {
        fun createRoute(detailId: Long) = "home/$detailId"
    }
}
