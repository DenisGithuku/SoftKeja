package com.denisgithuku.softkeja.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (
    val icon: ImageVector?,
    val route: String
        ) {
    object Home: Screen(Icons.Rounded.Home, "Home")
    object Details: Screen(null, "Details")
    object BookMarks: Screen(Icons.Rounded.Bookmarks, "Bookmarks")
    object About: Screen(null, "About")
    object Map: Screen(null, "Map")
    object Login: Screen(null, "Login")
    object SignUp: Screen(null, "SignUp")
    object ResetPassword: Screen(null, "Reset")
    object Splash: Screen(null, "Splash")
    object Profile: Screen(null, "Profile")
}
