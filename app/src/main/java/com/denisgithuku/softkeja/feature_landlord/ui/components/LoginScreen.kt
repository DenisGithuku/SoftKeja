package com.denisgithuku.softkeja.feature_landlord.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denisgithuku.softkeja.ui.theme.dimens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    firebaseAuth: FirebaseAuth,
    onLoggedIn: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Welcome back",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large))
        Login(
            onLogin = { email, password ->
                coroutineScope.launch {
                    try {
                        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                        if (result.user != null) {
                            onLoggedIn()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Could not login. Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }
}

@Composable
fun Login(onLogin: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordIsVisible by remember { mutableStateOf(false) }
    val formIsValid = derivedStateOf {
        email.contains('@') && password.length >= 6
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email,
                    contentDescription = "Email icon"
                )
            },
            trailingIcon = {
                AnimatedVisibility(visible = email.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            email = ""
                        }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Email address"
                        )
                    }
                }
            },
            label = {
                Text("Email")
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large))
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text("Password") },
            trailingIcon = {
                AnimatedVisibility(visible = passwordIsVisible) {
                    IconButton(onClick = { passwordIsVisible = !passwordIsVisible }) {
                        Icon(
                            imageVector = if (passwordIsVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = "Toggle password view"
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.VpnKey, contentDescription = "")
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password,
                imeAction = if (formIsValid.value) ImeAction.Done else ImeAction.None
            ),
            keyboardActions = KeyboardActions {
                if (formIsValid.value) {
                    onLogin(email, password)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large))
        Button(
            onClick = {
            if (formIsValid.value) {
                onLogin(email, password)
            }
        },
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Login")
        }
    }


}

@Preview
@Composable
fun LoginPreview() {
   Login(onLogin = { e, p -> })
}