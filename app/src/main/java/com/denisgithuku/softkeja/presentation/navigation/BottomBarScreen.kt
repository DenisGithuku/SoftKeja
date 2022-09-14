package com.denisgithuku.softkeja.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.denisgithuku.softkeja.R

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector?,
    @StringRes val resourceId: Int
) {
    object Home: Screen("home", Icons.Rounded.Home, R.string.home_id)
    object BookMarks: Screen("bookmarks", Icons.Rounded.Bookmarks, R.string.bookmarks_id)
}
