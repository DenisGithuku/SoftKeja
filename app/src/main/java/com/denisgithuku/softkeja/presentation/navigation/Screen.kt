package com.denisgithuku.softkeja.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.denisgithuku.softkeja.R

sealed class Screen (
    val route: String,
    val icon: ImageVector?,
    @StringRes val resourceId: Int
        ) {
    object Home: Screen("home", Icons.Rounded.Home, R.string.home_id)
    object Details: Screen("details",null, R.string.details_id)
    object BookMarks: Screen("bookmarks", Icons.Rounded.Bookmarks, R.string.bookmarks_id)
    object About: Screen("about",null, R.string.about_id)
    object Map: Screen("map", null, R.string.map_id)
    object Login: Screen("login", null, R.string.login_id)
    object SignUp: Screen("signup",null, R.string.signup_id)
    object ResetPassword: Screen("reset",null, R.string.reset_password_id)
    object Splash: Screen("splash", null, R.string.splash_id)
    object Profile: Screen("profile",null, R.string.profile_id)
}
