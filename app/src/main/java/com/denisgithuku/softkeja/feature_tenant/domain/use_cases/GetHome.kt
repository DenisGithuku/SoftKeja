package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetHome @Inject constructor(
    private val tenantRepository: TenantRepository
){
    suspend operator fun invoke(homeId: String): Flow<Resource<Home>> = flow {
        try {
            emit(Resource.Loading<Home>())
            val home = tenantRepository.getHomeDetails(homeId)?.toObject(Home::class.java)
            emit(Resource.Success<Home>(home))
        }catch (e: Exception) {
            emit(Resource.Error<Home>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}