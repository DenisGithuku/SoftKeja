package com.denisgithuku.softkeja.presentation.components.home

import com.denisgithuku.softkeja.common.util.UserMessage
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.domain.model.HomeCategory
import com.google.firebase.auth.FirebaseUser

data class HomeUiState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val selectedCategory: String = "",
    val categories: List<HomeCategory> = listOf(),
    val imageLoading: Boolean = false,
    var homes: List<Home> = listOf(),
    val userSignedIn: Boolean = false,
    val userGreeting: String? = null,
    val userMessages: MutableList<UserMessage> = mutableListOf(),
) {
    fun addToUserMessage(userMessage: UserMessage) {
        userMessages.add(userMessage)
    }

    fun clearUserMessages() {
        userMessages.clear()
    }
}
