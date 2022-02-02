package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_tenant.domain.model.Tenant
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateTenant @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenant: Tenant): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            emit(Resource.Success<Boolean>(tenantRepository.createTenant(tenant)))
        }catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }
}