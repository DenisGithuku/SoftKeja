package com.denisgithuku.softkeja.feature_landlord.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteHome @Inject constructor(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(homeId: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val deleted = landlordRepository.deleteHome(homeId)
            emit(Resource.Success<Boolean>(deleted))
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}