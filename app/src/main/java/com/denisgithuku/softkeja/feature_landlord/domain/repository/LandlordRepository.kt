package com.denisgithuku.softkeja.feature_landlord.domain.repository

import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_landlord.domain.model.Landlord
import com.denisgithuku.softkeja.feature_tenant.domain.model.Tenant
import com.google.firebase.firestore.DocumentSnapshot

interface LandlordRepository {

    suspend fun createLandlord(landlord: Landlord): Boolean

    suspend fun getLandlord(landlordId: String): DocumentSnapshot?

    suspend fun updateLandlord(landlordId: String, landLordHashMap: HashMap<String, Any>): Boolean

    suspend fun deleteLandlord(landlordId: String): Boolean

    suspend fun addHome(home: Home): Boolean

    suspend fun modifyHome(homeId: String, homeHashMap: HashMap<String, Any>): Boolean

    suspend fun deleteHome(homeId: String): Boolean

    suspend fun getHomeDetails(homeId: String): DocumentSnapshot?

    suspend fun addHomePhotos(photos: List<String>): Boolean
}