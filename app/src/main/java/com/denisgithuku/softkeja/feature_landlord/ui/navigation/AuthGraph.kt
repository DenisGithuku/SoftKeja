package com.denisgithuku.softkeja.feature_landlord.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.denisgithuku.softkeja.feature_landlord.ui.components.LoginScreen
import com.denisgithuku.softkeja.feature_landlord.ui.components.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(startDestination = "login", route = "login") {
        composable(route = "login") {
            LoginScreen(
                firebaseAuth = FirebaseAuth.getInstance(),
                onLoggedIn = {}
            )
            SignUpScreen (
                firebaseAuth = FirebaseAuth.getInstance(),
                onSignedUp = {}
            )
        }
    }
}