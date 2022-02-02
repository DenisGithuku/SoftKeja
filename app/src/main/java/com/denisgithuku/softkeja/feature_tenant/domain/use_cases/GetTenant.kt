package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_tenant.domain.model.Tenant
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTenant @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenantId: String): Flow<Resource<Tenant>>  = flow {
        try {
            emit(Resource.Loading<Tenant>())
            val tenant = tenantRepository.getTenant(tenantId)?.toObject(Tenant::class.java)
            emit(Resource.Success<Tenant>(tenant))
        }catch (e: Exception) {
            emit(Resource.Error<Tenant>(e.message.toString()))
        }
    }
}