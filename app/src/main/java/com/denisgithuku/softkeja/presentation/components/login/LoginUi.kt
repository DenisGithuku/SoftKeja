package com.denisgithuku.softkeja.presentation.components.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisgithuku.softkeja.presentation.theme.dimens
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    scaffoldState: ScaffoldState,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onLoggedIn: () -> Unit,
    onSignUpInstead: () -> Unit,
    onForgotPassword: () -> Unit
) {

    val uiState = loginViewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()

    var showDialog by remember {
        mutableStateOf(false)
    }

    val dialogProperties = DialogProperties(
        dismissOnBackPress = false, dismissOnClickOutside = false
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

    if (uiState.error.isNotEmpty()) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar(uiState.error)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = uiState.email,
            onValueChange = {
                loginViewModel.onEvent(LoginUiEvent.EmailChange(it))
            },
            shape = MaterialTheme.shapes.large,
            label = {
                Text(text = "Email address")
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.medium,
                    vertical = MaterialTheme.dimens.small
                )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))
        TextField(
            value = uiState.password,
            shape = MaterialTheme.shapes.large,
            onValueChange = {
                loginViewModel.onEvent(LoginUiEvent.PasswordChange(it))
            },
            label = {
                Text(text = "Password")
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                IconButton(onClick = {
                    loginViewModel.onEvent(LoginUiEvent.TogglePasswordVisibility)
                }) {

                    if (uiState.passwordVisible == PasswordVisibilityMode.VISIBLE) Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null
                    ) else Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.None,
            ),
            visualTransformation = if (uiState.passwordVisible == PasswordVisibilityMode.VISIBLE) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (uiState.formValid) {
                        showDialog = !showDialog
                        loginViewModel.onEvent(LoginUiEvent.Login)
                        if (uiState.loggedIn) {
                            showDialog = !showDialog
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Logged in as ${uiState.email}")
                            }
                            onLoggedIn()
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(uiState.error)
                            }
                        }
                    } else {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Form is not valid. Check for missing details")
                        }
                    }
                }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.medium,
                    vertical = MaterialTheme.dimens.small
                )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = {
            if (uiState.formValid) {
                showDialog = !showDialog
                loginViewModel.onEvent(LoginUiEvent.Login).also {
                    if (uiState.loggedIn) {
                        showDialog = !showDialog
                        showDialog = false
                        onLoggedIn()
                    }
                    if(uiState.error.isNotEmpty()) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(uiState.error)
                        }
                    }
                }
            } else {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Invalid form details. Check for any missing details")
                }
            }
        }) {
            Text(text = "Login")
        }
        SignUpInsteadSection(
            onSignUpInstead = onSignUpInstead,
            onForgotPassword = onForgotPassword
        )
    }
}

@Composable
fun SignUpInsteadSection(
    onSignUpInstead: () -> Unit,
    onForgotPassword: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            shape = RoundedCornerShape(32.dp),
            onClick = { onForgotPassword() }) {
            Text(text = "Forgot password")
        }
        OutlinedButton(
            shape = RoundedCornerShape(32.dp),
            onClick = { onSignUpInstead() }) {
            Text(text = "Register")
        }
    }
}

@Preview(showBackground = true)
@Composable fun SignUpInsteadPrev() {
    SignUpInsteadSection (onSignUpInstead = {}, onForgotPassword = {})
}
