package com.denisgithuku.softkeja.feature_landlord.domain.model

data class Home(
    val id: String = "",
    val homeCategory: String,
    val location: String = "",
    val description: String = "",
    val rent: String = "",
    val isAvailable: Boolean = true,
    val viewCount: Long = 0L
) {
    companion object {
        val homeFeatures = listOf(
            "Bathroom",
            "Kitchenette",
            "Garage",
            "Driveway",
            "Lawn",
            "Swimming pool",
            "Front porch",
            "Hearth"
        )
    }

    fun fromHomeCategoryToString(homeCategory: HomeCategory): String? {
        return try {
            val home = homeCategory.toString()
            home
        }catch (e: Exception) {
            null
        }
    }
    fun fromStringToHomeCategory(homeStringCategory: String): HomeCategory? {
        return try {
            val home = HomeCategory.valueOf(homeStringCategory)
            home
        }catch (e: Exception) {
            null
        }
    }
}
