package com.denisgithuku.softkeja.presentation.components.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.denisgithuku.softkeja.domain.model.User
import com.denisgithuku.softkeja.presentation.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    scaffoldState: ScaffoldState,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onSignedUp: () -> Unit
) {
    val uiState = signUpViewModel.uiState.collectAsState().value;
    var passwordIsVisible by remember {
        mutableStateOf(false)
    }
    var confirmPasswordIsVisible by remember {
        mutableStateOf(false)
    }
    val dialogProperties = DialogProperties(
        dismissOnBackPress = false, dismissOnClickOutside = false
    )
    var showDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

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
                        .background(Color.White)
                ) {
                    Column {
                        CircularProgressIndicator()
                        Text(text = "Please wait...")
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
            value = uiState.firstname,
            onValueChange = {
                signUpViewModel.onEvent(SignUpUiEvent.FirstnameChange(it))
            },
            placeholder = {
                Text(text = "Firstname")
            },
            shape = MaterialTheme.shapes.large,
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
            value = uiState.lastname,
            onValueChange = {
                signUpViewModel.onEvent(SignUpUiEvent.LastnameChange(it))
            },
            placeholder = {
                Text(text = "Lastname")
            },
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
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
            value = uiState.email,
            onValueChange = {
                signUpViewModel.onEvent(SignUpUiEvent.EmailChange(it))
            },
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(text = "Email address")
            },
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
            value = uiState.phone,
            onValueChange = {
                signUpViewModel.onEvent(SignUpUiEvent.PhoneChange(it))
            },
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None
            ),
            placeholder = {
                Text("Phone")
            },
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
            onValueChange = {
                signUpViewModel.onEvent(SignUpUiEvent.PasswordChange(it))
            },
            placeholder = {
                Text(text = "Password")
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordIsVisible = !passwordIsVisible
                }) {

                    if (passwordIsVisible) Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null
                    ) else Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
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
            value = uiState.confirmPassword,
            onValueChange = {
                signUpViewModel.onEvent(SignUpUiEvent.PasswordConfirmChange(it))
            },
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            trailingIcon = {
                IconButton(onClick = {
                    confirmPasswordIsVisible = !confirmPasswordIsVisible
                }) {

                    if (confirmPasswordIsVisible) Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null
                    ) else Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (confirmPasswordIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.None,
            ),
            placeholder = {
                Text(text = "Confirm password")
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    if (uiState.formIsValid) {
                        showDialog = !showDialog
                        val user = User(
                            uiState.userId,
                            uiState.firstname,
                            uiState.lastname,
                            uiState.email
                        )
                        signUpViewModel.onEvent(
                            SignUpUiEvent.SignUp
                        )
                        if(uiState.signUpSuccess) {
                            onSignedUp()
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(uiState.error)
                            }
                        }
                    } else {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Invalid details")
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
        Button(onClick = {
            if (uiState.formIsValid) {
                signUpViewModel.onEvent(
                    SignUpUiEvent.SignUp
                ).also {
                    if (uiState.signUpSuccess) {
                        scope.launch {
                            scaffoldState
                                .snackbarHostState
                                .showSnackbar("Registration success. Email verification link sent to ${uiState.email}")
                        }
                        onSignedUp()
                    }
                    if(uiState.error.isNotEmpty()) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(uiState.error)
                        }
                    }
                }
            } else {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Invalid form details. Check for missing values")
                }
            }
        }) {
            Text(text = "Register")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreview() {
    SignUpScreen(scaffoldState = rememberScaffoldState()) {

    }
}