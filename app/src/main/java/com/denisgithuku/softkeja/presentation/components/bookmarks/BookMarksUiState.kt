package com.denisgithuku.softkeja.presentation.components.bookmarks

import com.denisgithuku.softkeja.domain.model.Home

data class BookMarksUiState(
    val isLoading: Boolean = false,
    val bookmarks: List<Home> = listOf(),
    val error: String = ""
)