package com.denisgithuku.softkeja.presentation.components.signup

import com.denisgithuku.softkeja.domain.model.User

sealed class SignUpUiEvent {
    object SignUp: SignUpUiEvent()
    data class EmailChange(val email: String): SignUpUiEvent()
    data class PasswordChange(val password: String): SignUpUiEvent()
    data class PasswordConfirmChange(val confirmPassword: String): SignUpUiEvent()
    data class FirstnameChange(val firstname: String): SignUpUiEvent()
    data class LastnameChange(val lastname: String): SignUpUiEvent()
    data class PhoneChange(val phone: String): SignUpUiEvent()
}
