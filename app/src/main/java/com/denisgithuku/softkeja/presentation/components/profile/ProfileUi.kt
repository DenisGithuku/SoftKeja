package com.denisgithuku.softkeja.presentation.components.profile

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.BuildConfig
import com.denisgithuku.softkeja.presentation.components.details.TopRow
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun ProfileUi(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    onNavigateUp: () -> Unit,
    onSignOut: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState = profileViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.value.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (uiState.value.userMessages.isNotEmpty()) {
            for (userMessage in uiState.value.userMessages) {
                LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = userMessage.message?.message
                                ?: "Couldn't fetch profile. An error occurred",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
            return@Column
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                onNavigateUp()
            }, modifier = Modifier.fillMaxWidth(0.1f)) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }

            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = "Profile",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            )
        }



        Box(
            modifier = modifier
                .sizeIn(minWidth = 120.dp, minHeight = 120.dp)
                .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            val firstname = uiState.value.userProfile?.firstname
            Text(
                text = buildString {
                    append(firstname?.first()?.uppercase(Locale.getDefault()))
                    append(
                        uiState.value.userProfile?.firstname?.substringAfter(
                            firstname?.first().toString()
                        )
                    )
                },
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = modifier.height(10.dp))

        ProfileCard(
            email = uiState.value.userProfile?.email ?: "",
            fullname = uiState.value.userProfile?.firstname + " " + uiState.value.userProfile?.lastname,
            isPremium = uiState.value.userProfile?.isPremium ?: false,
            onSignOut = {
                profileViewModel.onEvent(ProfileUiEvent.OnSignOut).also {
                    onSignOut()
                }
            }
        )
    }
}

@Composable
fun ProfileCard(
    email: String,
    fullname: String,
    isPremium: Boolean,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.2f),
                RoundedCornerShape(size = 16.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Email: $email",
            style = TextStyle(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Name: $fullname",
            style = TextStyle(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "App version: ${BuildConfig.VERSION_NAME}",
            style = TextStyle(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
            )
        )

        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(
            visible = !isPremium,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(onClick = {}) {
                    Text(
                        text = "Upgrade to premium"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "(beta)"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onSignOut,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                text = "Sign out"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileCard(
        email = "githukudenis@gmail.com",
        fullname = "Denis Githuku",
        isPremium = false,
        onSignOut = {}
    )
}
