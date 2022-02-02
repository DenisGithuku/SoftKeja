package com.denisgithuku.softkeja.feature_tenant.domain.use_cases

import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_landlord.domain.model.HomeCategory
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.denisgithuku.softkeja.feature_tenant.domain.use_cases.util.SortType
import com.denisgithuku.softkeja.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetAllHomes @Inject constructor(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(homeCategory: HomeCategory): Flow<Resource<List<Home>>> = flow {
            try {
                emit(Resource.Loading<List<Home>>())
                val homes =
                    tenantRepository.getAllHomes()
                        .filter { docSnapShot ->
                            docSnapShot?.get("homeCategory") == homeCategory.toString()
                        }
                        .mapNotNull { docSnapShot ->
                        docSnapShot?.toObject(Home::class.java)
                    }
                emit(Resource.Success<List<Home>>(homes))
            } catch (e: Exception) {
                emit(Resource.Error<List<Home>>(e.message.toString()))
            }
    }.flowOn(Dispatchers.IO)
}