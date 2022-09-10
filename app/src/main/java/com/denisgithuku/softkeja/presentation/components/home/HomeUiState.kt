package com.denisgithuku.softkeja.presentation.components.home

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
    val error: String = ""
)
