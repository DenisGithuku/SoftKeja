package com.denisgithuku.softkeja.feature_landlord.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

class AddHomePhotos @Inject constructor(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(photos: List<String>): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val addedPhotos = landlordRepository.addHomePhotos(photos)
            emit(Resource.Success<Boolean>(addedPhotos))
        }catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}