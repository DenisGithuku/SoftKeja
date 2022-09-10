

package com.denisgithuku.softkeja.data.repository

import com.denisgithuku.softkeja.common.Constants.bookMarksCollection
import com.denisgithuku.softkeja.common.Constants.homeCategoryCollection
import com.denisgithuku.softkeja.common.Constants.homeCollection
import com.denisgithuku.softkeja.common.Resource
import com.denisgithuku.softkeja.domain.model.Home
import com.denisgithuku.softkeja.domain.model.HomeCategory
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : HomeRepository {
    override suspend fun getAllHomes(category: String): Flow<Resource<List<Home>>> = callbackFlow {
        trySend(Resource.Loading())
        val listenerRegistration = firestore
            .collection(homeCollection)
            .whereEqualTo("category", category)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message.toString()))
                }
                val homes = value?.documents?.mapNotNull {
                    it.toObject(Home::class.java).also { home ->
                        home?.features?.map {
                            value.documents.forEach { doc ->
                                doc.toObject(Home::class.java)?.features as List<Map<String, String>>
                            }
                        }
                    }
                }
                trySend(Resource.Success(homes))
            }
        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllHomeCategories(): Flow<Resource<List<HomeCategory>>> = callbackFlow {
        trySend(Resource.Loading())
        val listenerRegistration = firestore.collection(homeCategoryCollection)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message.toString()))
                    return@addSnapshotListener
                }
                val homeCategories = value?.documents?.mapNotNull {
                    it.toObject(HomeCategory::class.java)
                }
                trySend(Resource.Success(homeCategories))
            }
        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getHomeById(homeId: String): Flow<Resource<Home>> = callbackFlow {
        trySend(Resource.Loading())
        val listenerRegistration = firestore
            .collection(homeCollection)
            .document(homeId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    cancel(cause = error, message = error.message.toString())
                    trySend(Resource.Error(error.message.toString()))
                    return@addSnapshotListener
                }

                val home = value?.toObject(Home::class.java).also {
                    it?.features?.map {
                        value?.get("features") as List<Map<String, String>>
                    }
                }
                trySend(Resource.Success(home))
            }
        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getHomeImageUrl(homeImageRef: String): Flow<Resource<String>> =
        callbackFlow {
            val imagesRef = storage.reference
            trySend(Resource.Loading())
            imagesRef.child(homeImageRef.substringAfter(".com"))
                .downloadUrl
                .addOnSuccessListener { imageUri ->
                    trySend(Resource.Success(imageUri.toString()))
                }
                .addOnFailureListener {
                    trySend(Resource.Error(it.message.toString()))
                }
            awaitClose {
                close()
            }

        }

    override suspend fun addHomeToBookMarks(home: Home): Flow<Resource<Boolean>> = callbackFlow {
        try {
            trySend(Resource.Loading())
            firestore.collection(bookMarksCollection)
                .document(home.homeId)
                .set(home)
                .await()
            trySend(Resource.Success(true))
            awaitClose {
                close()
            }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getBookmarkedHomes(): Flow<Resource<List<Home>>> = callbackFlow {
        trySend(Resource.Loading())
        val listenerRegistration = firestore.collection(bookMarksCollection)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    cancel(cause = error, message = error.message.toString())
                    trySend(Resource.Error(error.message.toString()))
                    return@addSnapshotListener
                }
                val homes = value?.documents?.mapNotNull {
                    it.toObject(Home::class.java).also { home ->
                        home?.features?.map {
                            value.documents.map { doc ->
                                doc.toObject(Home::class.java)?.features as List<Map<String, String>>
                            }
                        }
                    }
                }
                trySend(Resource.Success(homes!!))
            }

        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }


}
