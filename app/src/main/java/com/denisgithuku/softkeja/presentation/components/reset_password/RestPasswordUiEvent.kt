package com.denisgithuku.softkeja.presentation.components.reset_password

sealed class ResetPasswordEvent {
    data class EmailChange(val email: String): ResetPasswordEvent()
    object Submit: ResetPasswordEvent()
}