package com.denisgithuku.softkeja.presentation.components.login

import androidx.annotation.StringRes
import com.denisgithuku.softkeja.R
import com.denisgithuku.softkeja.common.util.UserMessage

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val loggedIn: Boolean = false,
    val passwordVisible: PasswordVisibilityMode = PasswordVisibilityMode.GONE,
    val userMessages: MutableList<UserMessage> = mutableListOf()
) {
    fun addUserMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun clearUserMessages() {
        userMessages.clear()
    }
}

val LoginUiState.formValid: Boolean get()  {
    return email.isNotEmpty() && email.contains('@') && password.length >= 6 && password.isNotEmpty()
}

enum class PasswordVisibilityMode {
    VISIBLE,
    GONE
}
