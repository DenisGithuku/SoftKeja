@file:OptIn(ExperimentalCoroutinesApi::class)

package com.denisgithuku.softkeja.data.repository

import com.denisgithuku.softkeja.common.Constants
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.domain.model.User
import com.denisgithuku.softkeja.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): UserRepository {
    override suspend fun createUser(user: User): Flow<Resource<Boolean>> = callbackFlow {
            trySend(Resource.Loading())
            firestore
                .collection(Constants.userCollection)
                .document(user.userId)
                .set(user)
                .await()
            trySend(Resource.Success(true))
            awaitClose {
                close()
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUser(userId: String): Flow<Resource<User>> = callbackFlow {
            trySend(Resource.Loading())
            val listenerRegistration = firestore
                .collection(Constants.userCollection)
                .document(userId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        trySend(Resource.Error(error))
                    }
                    val user = value?.toObject(User::class.java)

                    trySend(Resource.Success(user))
                }
            awaitClose {
                listenerRegistration.remove()
                close()
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUser(
        userId: String,
        userHashMap: HashMap<String, Any>
    ): Flow<Resource<Boolean>> = callbackFlow {
            trySend(Resource.Loading())
            firestore
                .collection(Constants.userCollection)
                .document(userId)
                .set(userHashMap, SetOptions.merge())
                .await()
            trySend(Resource.Success(true))
            awaitClose {
                close()
            }
        }.flowOn(Dispatchers.IO)


    override suspend fun deleteUser(userId: String): Flow<Resource<Boolean>> = callbackFlow {
            trySend(Resource.Loading())
            firestore
                .collection(Constants.userCollection)
                .document(userId)
                .delete()
                .await()
            trySend(Resource.Success(true))
            awaitClose {
                close()
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
