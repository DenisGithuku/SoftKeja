package com.denisgithuku.softkeja.presentation.components.profile

import com.denisgithuku.softkeja.common.util.UserMessage

data class ProfileUiState (
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val userMessages: MutableList<UserMessage> = mutableListOf(),
) {
    fun addToUserMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun clearUserMessages() {
        userMessages.clear()
    }
}

data class UserProfile(
    val userId: String? = null,
    val email: String? = null,
    val isPremium: Boolean = false,
    val firstname: String? = null,
    val lastname: String? = null,
    )
