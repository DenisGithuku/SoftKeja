package com.denisgithuku.softkeja.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.denisgithuku.softkeja.presentation.components.common.BottomBar
import com.denisgithuku.softkeja.presentation.navigation.MainNavGraph
import com.denisgithuku.softkeja.presentation.navigation.Screen
import com.denisgithuku.softkeja.presentation.theme.SoftKejaTheme
import com.denisgithuku.softkeja.presentation.theme.dimens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var userLoggedIn: Boolean = false

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {

            SoftKejaTheme {
                val screens = listOf(
                    Screen.Home,
                    Screen.BookMarks
                )

                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination =
                    navBackStackEntry?.destination?.route?.substringBefore("/")
                val sheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

                ModalBottomSheetLayout(
                    sheetContent = {
                        ModalBottomSheetContent()
                    },
                    sheetShape = RoundedCornerShape(16.dp),
                    sheetBackgroundColor = MaterialTheme.colors.surface,
                    sheetElevation = 12.dp,
                    sheetState = sheetState
                ) {

                }
                Scaffold(
                    scaffoldState = scaffoldState,
                    bottomBar = {
                        if (currentDestination == Screen.Home.route || currentDestination == Screen.BookMarks.route) {
                            BottomBar(
                                currentDestination = navBackStackEntry?.destination,
                                screens = screens,
                                onNavigate = { destination ->
                                    navController.navigate(destination.route) {
                                        navController.graph.startDestinationRoute?.let { screen_route ->
                                            popUpTo(screen_route) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) {
                    MainNavGraph(
                        scaffoldState = scaffoldState,
                        isLoggedIn = userLoggedIn,
                        navController = navController,
                        sheetState = sheetState
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

@Composable
fun ModalBottomSheetContent() {
    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 1.dp)
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.medium),
    ) {
        Row(
            modifier = Modifier.clickable {

            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Rounded.Info, contentDescription = null)
            Text(
                text = "About"
            )
        }
    }
}
