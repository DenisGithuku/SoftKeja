package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddToFavouriteHomes @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(home: Home): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val hasBeenAddedToFavouriteHomes = tenantRepository.addToFavouriteHomes(home)
            emit(Resource.Success<Boolean>(hasBeenAddedToFavouriteHomes))
        }catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}