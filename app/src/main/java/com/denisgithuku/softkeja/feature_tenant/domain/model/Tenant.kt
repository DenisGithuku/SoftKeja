package com.denisgithuku.softkeja.feature_tenant.domain.model

data class Tenant(
    val id: String,
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val location: String = "",
)
