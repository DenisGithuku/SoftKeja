package com.denisgithuku.softkeja.presentation.components.profile

sealed interface ProfileUiEvent {
    object OnSignOut : ProfileUiEvent
}
