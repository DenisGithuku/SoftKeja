package com.denisgithuku.softkeja.domain.model

data class User(
    var userId: String = "",
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var isPremium: Boolean = false,
    var bookMarks: List<String> = emptyList()
)

val User.canSaveToFavourites: Boolean get() = isPremium
