package com.denisgithuku.softkeja.feature_landlord.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHomePhotos @Inject constructor(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(homeId: String): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading<List<String>>())
            val photos = landlordRepository.getHomePhotos(homeId)?.map {
                it.path
            }
            emit(Resource.Success<List<String>>(photos))
        }catch (e: Exception) {
            emit(Resource.Error<List<String>>(e.message.toString()))
        }
    }
}