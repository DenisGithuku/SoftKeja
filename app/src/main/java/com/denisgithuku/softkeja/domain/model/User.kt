package com.denisgithuku.softkeja.domain.model

data class User(
    var userId: String = "",
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var isPremium: Boolean = false
)

val User.canSaveToFavourites: Boolean get() = isPremium