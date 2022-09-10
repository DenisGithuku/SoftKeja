package com.denisgithuku.softkeja.presentation.components.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
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
                                            error = task.exception?.message.toString()
                                        )
                                    }
                                }
                            }
                    }
                }catch (e: Exception) {
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