package com.denisgithuku.softkeja.feature_tenant.domain.use_cases.util

sealed class SortType {
    object None: SortType()
    object Viewed: SortType()
    object Recommended: SortType()
}
