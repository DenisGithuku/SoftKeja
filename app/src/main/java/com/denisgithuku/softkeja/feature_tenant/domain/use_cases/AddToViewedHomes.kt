package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import javax.inject.Inject

class AddToViewedHomes @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(home: Home): Boolean {
        return tenantRepository.addToFavouriteHomes(home)
    }
}