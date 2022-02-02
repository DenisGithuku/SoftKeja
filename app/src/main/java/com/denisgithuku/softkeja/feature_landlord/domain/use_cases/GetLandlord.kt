package com.denisgithuku.softkeja.feature_landlord.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.model.Landlord
import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

class GetLandlord @Inject constructor(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(landlordId: String): Flow<Resource<Landlord>> = flow {
        try {
            emit(Resource.Loading<Landlord>())
            val landlord = landlordRepository.getLandlord(landlordId)!!.toObject(Landlord::class.java)
            emit(Resource.Success<Landlord>(landlord))
        }catch (e: Exception) {
            emit(Resource.Error<Landlord>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}