package com.denisgithuku.softkeja.feature_landlord.data.repository

import android.net.Uri
import com.denisgithuku.softkeja.feature_landlord.LandlordConstants
import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_landlord.domain.model.Landlord
import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.denisgithuku.softkeja.feature_tenant.domain.use_cases.util.TenantsConstants
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LandlordRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseCrashlytics: FirebaseCrashlytics
): LandlordRepository {
    override suspend fun createLandlord(landlord: Landlord): Boolean {
        return try {
            firebaseFirestore
                .collection(LandlordConstants.landlordCollection)
                .document(landlord.id)
                .set(landlord)
                .await()
            true
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            false
        }
    }

    override suspend fun getLandlord(landlordId: String): DocumentSnapshot? {
        return try {
            val doc = firebaseFirestore
                .collection(LandlordConstants.landlordCollection)
                .document(landlordId)
                .get()
                .await()
            doc
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            null
        }
    }

    override suspend fun updateLandlord(
        landlordId: String,
        landLordHashMap: HashMap<String, Any>
    ): Boolean {
        return try {
            firebaseFirestore
                .collection(LandlordConstants.landlordCollection)
                .document(landlordId)
                .set(landLordHashMap, SetOptions.merge())
                .await()
            true
        }catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            false
        }
    }

    override suspend fun deleteLandlord(landlordId: String): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.TenantCollection)
                .document(landlordId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            false
        }
    }

    override suspend fun addHome(home: Home, photos: List<Uri>): Boolean {
        return try {
            val homeId = firebaseFirestore.collection(LandlordConstants.homeCollection).document().id
            firebaseFirestore
                .collection(LandlordConstants.homeCollection)
                .document(homeId)
                .set(home)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val photosRef = firebaseStorage.reference
                        photos.forEach { path ->
                            photosRef
                                .child("/images/${homeId}")
                                .putFile(path)
                        }
                    }
                }
            true
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            false
        }
    }

    override suspend fun modifyHome(homeId: String, homeHashMap: HashMap<String, Any>): Boolean {
        return try {
            firebaseFirestore
                .collection(LandlordConstants.homeCollection)
                .document(homeId)
                .set(homeHashMap)
                .await()
            true
        }catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            false
        }
    }

    override suspend fun deleteHome(homeId: String): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.TenantCollection)
                .document(homeId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            false
        }
    }

    override suspend fun getHomeDetails(homeId: String): DocumentSnapshot? {
        return try {
            val doc = firebaseFirestore
                .collection(LandlordConstants.homeCollection)
                .document(homeId)
                .get()
                .await()
                .also {
                    getHomePhotos(homeId)
                }
            doc
        }catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            null
        }
    }

    override suspend fun getHomePhotos(homeId: String): List<StorageReference>? {
        return try {
            val imagesRef = firebaseStorage.reference.child("/images/${homeId}")
            val images = imagesRef.listAll().await().items
            images
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(LandlordConstants.landlordRepoException, e.message.toString())
            null
        }

    }
}