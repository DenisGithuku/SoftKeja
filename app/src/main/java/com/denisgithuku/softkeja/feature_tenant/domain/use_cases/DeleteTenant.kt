package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteTenant @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenantId: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            emit(Resource.Success<Boolean>(tenantRepository.deleteTenant(tenantId)))
        }catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }
}