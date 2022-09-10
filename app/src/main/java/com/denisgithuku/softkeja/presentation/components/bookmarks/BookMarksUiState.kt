package com.denisgithuku.softkeja.presentation.components.bookmarks

import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.model.Home

data class BookMarksUiState(
    val isLoading: Boolean = false,
    val bookmarks: List<Home> = listOf(),
    val userMessages: MutableList<UserMessage> = mutableListOf(),
) {
    fun addToUserMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun clearUserMessages() {
        userMessages.clear()
    }
}
