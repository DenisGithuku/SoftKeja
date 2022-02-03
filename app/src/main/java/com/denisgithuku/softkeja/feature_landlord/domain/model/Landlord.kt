package com.denisgithuku.softkeja.feature_landlord.domain.model

data class Landlord(
    val id: String,
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val location: String = "",
)
