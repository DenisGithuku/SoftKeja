package com.denisgithuku.softkeja.presentation.components.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.model.User
import com.denisgithuku.softkeja.domain.repository.UserRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> get() = _uiState

    fun onEvent(uiEvent: SignUpUiEvent) {
        when (uiEvent) {
            is SignUpUiEvent.FirstnameChange -> {
                firstNameChange(uiEvent.firstname)
            }
            is SignUpUiEvent.LastnameChange -> {
                lastNameChange(uiEvent.lastname)
            }
            is SignUpUiEvent.PhoneChange -> {
                phoneChange(uiEvent.phone)
            }
            is SignUpUiEvent.SignUp -> {
                signup()
            }
            is SignUpUiEvent.EmailChange -> {
                emailChange(uiEvent.email)
            }
            is SignUpUiEvent.PasswordChange -> {
                passwordChange(uiEvent.password)
            }
            is SignUpUiEvent.PasswordConfirmChange -> {
                confirmPasswordChange(uiEvent.confirmPassword)
            }
        }
    }

    private fun signup() {
        try {
            auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        _uiState.update { it.copy(userId = authResult.result?.user!!.uid) }
                        val user = User(
                            uiState.value.userId,
                            uiState.value.firstname,
                            uiState.value.lastname,
                            uiState.value.email
                        )
                        viewModelScope.launch {
                            userRepository.createUser(user).collect { result ->
                                _uiState.value.clearUserMessages()
                                when (result) {
                                    is Resource.Loading -> {
                                        _uiState.update {
                                            it.copy(
                                                isLoading = true
                                            )
                                        }
                                    }
                                    is Resource.Success -> {
                                        _uiState.update {
                                            it.copy(
                                                isLoading = false,
                                                signUpSuccess = result.data!!
                                            )
                                        }
                                        authResult.result.user?.sendEmailVerification()?.await()
                                    }
                                    is Resource.Error -> {
                                        _uiState.update {
                                            it.copy(
                                                isLoading = false,
                                            )
                                        }.also {
                                            _uiState.value.addUserMessage(UserMessage(message = result.error))
                                        }
                                    }

                                }
                            }
                        }
                    } else {
                        _uiState.value.addUserMessage(UserMessage(message = authResult.exception))
                    }
                }
        } catch (e: Exception) {
            when (e) {
                is FirebaseTooManyRequestsException -> {
                    _uiState.value.addUserMessage(
                        UserMessage(message = e)
                    )
                }
                is FirebaseAuthUserCollisionException -> {
                    _uiState.value.addUserMessage(
                        UserMessage(message = e)
                    )
                }
                is FirebaseAuthWeakPasswordException -> {
                    _uiState.value.addUserMessage(
                        UserMessage(e)
                    )
                }
                is FirebaseNetworkException -> {
                    _uiState.value.addUserMessage(
                        UserMessage(
                            e
                        )
                    )
                }
                else -> {
                    _uiState.value.addUserMessage(
                        UserMessage(e)
                    )
                }
            }
        }
    }

    private fun firstNameChange(firstname: String) {
        _uiState.update {
            it.copy(
                firstname = firstname
            )
        }
    }

    private fun lastNameChange(lastname: String) {
        _uiState.update {
            it.copy(
                lastname = lastname
            )
        }
    }

    private fun phoneChange(phone: String) {
        _uiState.update {
            it.copy(
                phone = phone
            )
        }
    }

    private fun emailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    private fun passwordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    private fun confirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
    }


}
