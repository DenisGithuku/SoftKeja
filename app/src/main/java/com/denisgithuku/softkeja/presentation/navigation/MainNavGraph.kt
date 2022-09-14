package com.denisgithuku.softkeja.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun MainNavGraph(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    isLoggedIn: Boolean,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) + expandIn()
            },
            exitTransition = {
                fadeOut() +  slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Start)
            }
        ) {
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
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
            }
        ) {
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
        composable(
            route = Screen.SignUp.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.End)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
            }
        ) {
            SignUpScreen(scaffoldState = scaffoldState, onSignedUp = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.SignUp.route) {
                        inclusive = true
                    }
                }
            })
        }
        composable(
            Screen.Home.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700)) + expandIn()
            },
            exitTransition = {
                fadeOut() + shrinkOut()
            }
        ) {
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
        composable(
            Screen.ResetPassword.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, tween(700))
            }
        ) {
            ResetPasswordScreen(scaffoldState = scaffoldState, onResetPassword = {
                navController.popBackStack()
            })
        }

        composable(
            Screen.Details.route + "/{homeId}",
            enterTransition = {
                fadeIn() + expandIn()
            },
            exitTransition = {
                fadeOut() + shrinkOut()
            }
        ) {
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

        composable(
            Screen.BookMarks.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, tween(700))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, tween(700))
            }
        ) {
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

        composable(
            Screen.Map.route + "/{latLng}",
            enterTransition = {
                fadeIn(tween(700)) + slideIntoContainer(AnimatedContentScope.SlideDirection.Up)
            },
            exitTransition = {
                fadeOut(tween(700)) + slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
            }
        ) {
            MapUi(scaffoldState = scaffoldState)
        }

        composable(
            Screen.Profile.route + "/{userId}",
            enterTransition =  {
                fadeIn() +  slideIntoContainer(AnimatedContentScope.SlideDirection.Up, tween(700))
            },
            exitTransition = {
               fadeOut() +  slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, tween(700))
            }
        ) {
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
