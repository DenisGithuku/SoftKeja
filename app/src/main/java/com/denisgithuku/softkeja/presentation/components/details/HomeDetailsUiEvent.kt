package com.denisgithuku.softkeja.presentation.components.details

import com.denisgithuku.softkeja.domain.model.Home

sealed class HomeDetailsUiEvent {
    class BookMarkHome(val home: Home): HomeDetailsUiEvent()
}
