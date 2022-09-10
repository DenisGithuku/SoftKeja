package com.denisgithuku.softkeja.presentation.components.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.Login -> {
                authenticate()
            }
            is LoginUiEvent.TogglePasswordVisibility -> {
                togglePasswordVisibilityMode()
            }
            is LoginUiEvent.EmailChange -> {
                updateEmail(event.email)
            }
            is LoginUiEvent.PasswordChange -> {
                updatePassword(event.password)
            }
        }
    }

    private fun togglePasswordVisibilityMode() {
        val visibilityMode = uiState.value.passwordVisible
        val newMode = if (visibilityMode == PasswordVisibilityMode.VISIBLE) {
            PasswordVisibilityMode.GONE
        } else {
            PasswordVisibilityMode.VISIBLE
        }
        _uiState.update {
            it.copy(
                passwordVisible = newMode
            )
        }
    }

    private fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
                try {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password).await()
                    if (auth.currentUser?.isEmailVerified == true) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loggedIn = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loggedIn = false,
                                error = "Email not verified"
                            )
                        }
                    }

                } catch (e: Exception) {
                    when (e) {
                        is FirebaseAuthInvalidUserException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "No such user found. Have you created an account?"
                                )
                            }
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Could not authenticate. Make sure to provide the correct details."
                                )
                            }
                        }
                        is FirebaseNetworkException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Could not login. Please check your internet connection"
                                )
                            }
                        }
                        else -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message.toString()
                                )
                            }
                        }
                    }
            }
        }
    }
}