package com.denisgithuku.softkeja.presentation.components.login

sealed class LoginUiEvent {
    object Login: LoginUiEvent()
    object TogglePasswordVisibility: LoginUiEvent()
    data class EmailChange(val email: String): LoginUiEvent()
    data class PasswordChange(val password: String): LoginUiEvent()
}

