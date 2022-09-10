package com.denisgithuku.softkeja.presentation.components.details

import com.denisgithuku.softkeja.domain.model.Home

sealed class HomeDetailsUiEvent {
    data class BookMarkHome(val home: Home): HomeDetailsUiEvent()
}
