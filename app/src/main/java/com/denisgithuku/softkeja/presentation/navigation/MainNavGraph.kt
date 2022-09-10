package com.denisgithuku.softkeja.presentation.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.denisgithuku.softkeja.presentation.components.about.AboutUi
import com.denisgithuku.softkeja.presentation.components.bookmarks.BookMarksUi
import com.denisgithuku.softkeja.presentation.components.details.HomeDetailsUi
import com.denisgithuku.softkeja.presentation.components.home.HomeUi
import com.denisgithuku.softkeja.presentation.components.login.LoginScreen
import com.denisgithuku.softkeja.presentation.components.map.MapUi
import com.denisgithuku.softkeja.presentation.components.profile.ProfileUi
import com.denisgithuku.softkeja.presentation.components.reset_password.ResetPasswordScreen
import com.denisgithuku.softkeja.presentation.components.signup.SignUpScreen
import com.denisgithuku.softkeja.presentation.components.splash.SplashUi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun MainNavGraph(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    isLoggedIn: Boolean,
    sheetState: ModalBottomSheetState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashUi(onSplashScreenShow = {
                if (isLoggedIn) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            })
        }
        composable(route = Screen.Login.route) {
            LoginScreen(scaffoldState = scaffoldState, onLoggedIn = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }, onSignUpInstead = {
                navController.navigate(Screen.SignUp.route) {
                    popUpTo(Screen.SignUp.route) {
                        inclusive = true
                    }
                }
            },
                onForgotPassword = {
                    navController.navigate(Screen.ResetPassword.route) {
                        popUpTo(Screen.ResetPassword.route) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(scaffoldState = scaffoldState, onSignedUp = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.SignUp.route) {
                        inclusive = true
                    }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeUi(
                onOpenHome = { home ->
                    navController.navigate(Screen.Details.route + "/${home.homeId}") {
                        popUpTo(Screen.Details.route) {
                            inclusive = true
                        }
                        restoreState = true
                    }
                },
                scaffoldState = scaffoldState,
                sheetState = sheetState,
                onNavigateToProfile = { userId ->
                    navController.navigate(Screen.Profile.route + "/${userId}") {
                        popUpTo(Screen.Profile.route) {
                            inclusive = true
                        }
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(scaffoldState = scaffoldState, onResetPassword = {
                navController.popBackStack()
            })
        }

        composable(Screen.Details.route + "/{homeId}") {
            HomeDetailsUi(
                scaffoldState = scaffoldState,
                onViewOnMap = { latLng ->
                    navController.navigate(Screen.Map.route + "/${latLng}") {
                        popUpTo(Screen.Map.route) {
                            inclusive = true
                        }
                        restoreState = true
                    }
                }, onNavigateUp = {
                    navController.popBackStack()
                })
        }

        composable(Screen.BookMarks.route) {
            BookMarksUi(scaffoldState = scaffoldState, onOpenHome = { home ->
                navController.navigate(Screen.Details.route + "/${home.homeId}") {
                    popUpTo(Screen.Details.route) {
                        inclusive = true
                    }
                    restoreState
                }
            })
        }
        composable(Screen.About.route) {
            AboutUi(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Profile.route) {
                            inclusive = true
                        }
                        restoreState = true
                    }
                }
            )
        }

        composable(Screen.Map.route + "/{latLng}") {
            MapUi(scaffoldState = scaffoldState)
        }

        composable(Screen.Profile.route + "/{userId}") {
            ProfileUi(scaffoldState = scaffoldState, onSignOut = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Profile.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = false
                }
            }, onNavigateUp = {
                navController.popBackStack()
            })
        }

    }

}
