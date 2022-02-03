package com.denisgithuku.softkeja.feature_landlord.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ModifyHome @Inject constructor(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(
        homeId: String,
        homeHashMap: HashMap<String, Any>
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val modifiedSuccessfully = landlordRepository.modifyHome(homeId, homeHashMap)
            emit(Resource.Success<Boolean>(modifiedSuccessfully))
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}