package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import javax.inject.Inject

class UpdateTenant @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenantId: String, tenantHashMap: HashMap<String, Any>): Boolean {
        return tenantRepository.updateTenant(tenantId, tenantHashMap)
    }
}