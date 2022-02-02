package com.denisgithuku.softkeja.feature_tenant.domain.model

data class Tenant(
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val idNo: String = "",
    val location: String = "",
)
