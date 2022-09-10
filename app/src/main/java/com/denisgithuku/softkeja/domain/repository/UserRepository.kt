package com.denisgithuku.softkeja.domain.repository

import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun createUser(user: User): Flow<Resource<Boolean>>

    suspend fun getUser(userId: String): Flow<Resource<User>>

    suspend fun updateUser(userId: String, userHashMap: HashMap<String, Any>): Flow<Resource<Boolean>>

    suspend fun deleteUser(userId: String): Flow<Resource<Boolean>>

    suspend fun logout()
}
