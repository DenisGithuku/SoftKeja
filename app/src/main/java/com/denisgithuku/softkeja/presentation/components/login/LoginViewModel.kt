package com.denisgithuku.softkeja.presentation.components.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.util.UserMessage
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
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
                    val user = auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password).await().user
                    user?.let {
                        if (it.isEmailVerified) {
                            _uiState.update { loginUiState ->
                                loginUiState.copy(
                                    isLoading = false,
                                    loggedIn = true
                                )
                            }
                        } else {
                            _uiState.update { loginUiState ->
                                loginUiState.copy(
                                    isLoading = false,
                                    loggedIn = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(
                                    UserMessage(message = Throwable(message = "Email not verified."))
                                )
                            }
                        }
                    }

                } catch (e: Exception) {
                    _uiState.value.clearUserMessages()
                    when (e) {
                        is FirebaseAuthInvalidUserException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(UserMessage(e))
                            }
                        }
                        is FirebaseTooManyRequestsException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(UserMessage(message = e))
                            }
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(UserMessage(e))
                            }
                        }
                        is FirebaseNetworkException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(UserMessage(e))
                            }
                        }
                        else -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(UserMessage(e))
                            }
                        }
                    }
            }
        }
    }
}
