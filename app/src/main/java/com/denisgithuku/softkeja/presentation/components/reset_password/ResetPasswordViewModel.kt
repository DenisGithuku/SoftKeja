package com.denisgithuku.softkeja.presentation.components.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.util.UserMessage
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> get() = _uiState


    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.Submit -> {
                try {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                        auth.sendPasswordResetEmail(_uiState.value.email)
                            .addOnCompleteListener { task ->
                                _uiState.value.clearUserMessages()
                                if (task.isSuccessful) {
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            passwordResetEmailSent = true
                                        )
                                    }
                                } else {
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            passwordResetEmailSent = false,
                                        )
                                    }.also {
                                        _uiState.value.addUserMessage(
                                            UserMessage(message = task.exception)
                                        )
                                    }
                                }
                            }
                    }
                }catch (e: Exception) {
                    _uiState.value.clearUserMessages()
                    when (e) {
                        is FirebaseAuthInvalidUserException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(UserMessage(message = e))
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
                                _uiState.value.addUserMessage(
                                    UserMessage(e)
                                )
                            }
                        }
                        is FirebaseNetworkException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(
                                    UserMessage(e)
                                )
                            }
                        }
                        else -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }.also {
                                _uiState.value.addUserMessage(
                                    UserMessage(e)
                                )
                            }
                        }
                    }
                }
            }
            is ResetPasswordEvent.EmailChange -> {
               updateEmail(event.email)
            }
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }
}
