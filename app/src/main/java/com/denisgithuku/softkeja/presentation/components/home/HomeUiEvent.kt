package com.denisgithuku.softkeja.presentation.components.home

import com.denisgithuku.softkeja.domain.model.Home

sealed class HomeUiEvent {
    object SignOut : HomeUiEvent()
    class FilterHomes(val filter: String) : HomeUiEvent()
}
