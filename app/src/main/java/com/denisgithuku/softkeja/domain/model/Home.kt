package com.denisgithuku.softkeja.domain.model

data class Home(
    var available: Boolean = false,
    var category: String = "",
    var features: List<String> = listOf(),
    var homeId: String = "",
    var imageUrl: String = "",
    var location: String = "",
    val monthly_rent: String = "",
    var name: String = "",
    var telephone: String = "",
)