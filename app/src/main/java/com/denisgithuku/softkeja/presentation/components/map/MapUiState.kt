package com.denisgithuku.softkeja.presentation.components.map

data class MapUiState(
    val isLoading: Boolean = false,
    val latLng: String = "",
    val error: String = ""
)
