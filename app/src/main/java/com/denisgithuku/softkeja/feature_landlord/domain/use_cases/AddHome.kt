package com.denisgithuku.softkeja.feature_landlord.domain.use_cases

import android.net.Uri
import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddHome @Inject constructor(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(home: Home, photos: List<Uri>): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val homeAdded = landlordRepository.addHome(home, photos)
            emit(Resource.Success<Boolean>(homeAdded))
        }catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}