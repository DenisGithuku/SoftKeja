package com.denisgithuku.softkeja.presentation.components.reset_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.presentation.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    scaffoldState: ScaffoldState,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
    onResetPassword: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val uiState = resetPasswordViewModel.uiState.collectAsState().value
    var showDialog by remember {
        mutableStateOf(false)
    }
    val dialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    )

    if (uiState.isLoading) {
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = !showDialog },
                properties = dialogProperties
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.White, MaterialTheme.shapes.large)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading..")
                    }
                }

            }
        }
    }

    if (uiState.userMessages.isNotEmpty()) {
        for (userMessage in uiState.userMessages) {
            LaunchedEffect(scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(userMessage.message.toString())
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Request for a change of password",
            style = TextStyle(fontSize = 25.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = Color.Black.copy(alpha = 0.8f))
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            value = uiState.email,
            onValueChange = { resetPasswordViewModel.onEvent(ResetPasswordEvent.EmailChange(it)) },
            placeholder = {
                Text(text = "Email address")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.dimens.large,
                    horizontal = MaterialTheme.dimens.medium
                ),
            maxLines = 1,
            singleLine = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (uiState.formValid) {
                    showDialog != showDialog
                    resetPasswordViewModel.onEvent(ResetPasswordEvent.Submit)
                        .also {
                            if (uiState.passwordResetEmailSent) {
                                showDialog = !showDialog
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Password reset link sent to $uiState.email")
                                }
                                onResetPassword()
                            }
                            if (uiState.userMessages.isNotEmpty()) {
                                for (userMessage in uiState.userMessages) {
                                    showDialog = !showDialog
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(userMessage.message.toString())
                                    }
                                }
                            }
                        }
                } else {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Invalid email address")
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Reset")
        }
    }
}
