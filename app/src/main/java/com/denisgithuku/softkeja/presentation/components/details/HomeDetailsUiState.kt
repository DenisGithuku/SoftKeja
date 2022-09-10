package com.denisgithuku.softkeja.presentation.components.details

import com.denisgithuku.softkeja.domain.model.Home

data class HomeDetailsUiState(
    val isLoading: Boolean = false,
    val home: Home = Home(),
    val hasBookmarked: Boolean = false,
    val error: String = ""
)
