package com.denisgithuku.softkeja.presentation.components.reset_password

import com.denisgithuku.softkeja.common.util.UserMessage

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val passwordResetEmailSent: Boolean = false,
    val userMessages: MutableList<UserMessage> = mutableListOf()
) {
    fun addUserMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun clearUserMessages() {
        userMessages.clear()
    }
}

val ResetPasswordUiState.formValid: Boolean get() = email.isNotEmpty() && email.contains('@')
