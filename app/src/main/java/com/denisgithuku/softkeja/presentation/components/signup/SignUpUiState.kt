package com.denisgithuku.softkeja.presentation.components.signup

import com.denisgithuku.softkeja.presentation.components.reset_password.PasswordRequirement

data class SignUpState(
    val isLoading: Boolean = false,
    val userId: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordRequirements: List<PasswordRequirement> = listOf(),
    val signUpSuccess: Boolean = false,
    val error: String = ""
)

val SignUpState.formIsValid: Boolean
    get() = firstname.isNotEmpty()
            && lastname.isNotEmpty()
            && email.isNotEmpty()
            && email.contains('@')
            && phone.isNotEmpty()
            && password.isNotEmpty()
            && confirmPassword.isNotEmpty()
            && password == confirmPassword
