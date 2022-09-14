package com.denisgithuku.softkeja.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.denisgithuku.softkeja.presentation.components.common.BottomBar
import com.denisgithuku.softkeja.presentation.navigation.BottomBarScreen
import com.denisgithuku.softkeja.presentation.navigation.MainNavGraph
import com.denisgithuku.softkeja.presentation.navigation.Screen
import com.denisgithuku.softkeja.presentation.theme.SoftKejaTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    private var userLoggedIn: Boolean = false

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SoftKejaTheme {
                val screens = listOf(
                    Screen.Home,
                    Screen.BookMarks
                )


                val scaffoldState = rememberScaffoldState()
                val navController = rememberAnimatedNavController()

                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        val navBackStack by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStack?.destination

                        if (currentDestination?.route == BottomBarScreen.Home.route || currentDestination?.route === BottomBarScreen.BookMarks.route) {
                            BottomBar(
                                currentDestination = currentDestination,
                                screens = screens,
                                onNavigate = { destination ->
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                })
                        }
                    }
                ) {
                    MainNavGraph(
                        scaffoldState = scaffoldState,
                        isLoggedIn = userLoggedIn,
                        navController = navController,
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            userLoggedIn = true
        }
    }
}
