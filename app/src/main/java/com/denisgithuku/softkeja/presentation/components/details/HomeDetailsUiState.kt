package com.denisgithuku.softkeja.presentation.components.details

import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.presentation.components.profile.UserProfile

data class HomeDetailsUiState(
    val isLoading: Boolean = false,
    val home: Home = Home(),
    val hasBookmarked: Boolean = false,
    val userId: String? = null,
    val bookMarks: List<Home> = emptyList(),
    val userMessages: MutableList<UserMessage> = mutableListOf()
) {
    fun addUserMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun clearUserMessages() {
        userMessages.clear()
    }
}
