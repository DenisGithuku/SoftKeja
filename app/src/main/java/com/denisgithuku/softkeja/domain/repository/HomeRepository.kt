package com.denisgithuku.softkeja.domain.repository

import android.net.Uri
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.domain.model.HomeCategory
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getAllHomes(category: String): Flow<Resource<List<Home>>>

    suspend fun getAllHomeCategories(): Flow<Resource<List<HomeCategory>>>

    suspend fun getHomeById(homeId: String): Flow<Resource<Home>>

    suspend fun getHomeImageUrl(homeImageRef: String): Flow<Resource<String>>

    suspend fun addHomeToBookMarks(userId: String, home: Home): Flow<Resource<Boolean>>

    suspend fun getBookmarkedHomes(): Flow<List<Home>>

}
