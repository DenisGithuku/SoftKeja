package com.denisgithuku.softkeja.presentation.components.reset_password

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val passwordResetEmailSent: Boolean = false,
    val error: String = ""
)

val ResetPasswordUiState.formValid: Boolean get() = email.isNotEmpty() && email.contains('@')