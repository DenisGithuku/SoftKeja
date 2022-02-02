package com.denisgithuku.softkeja.feature_tenant.domain.repository

import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_tenant.domain.model.Tenant
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface TenantRepository {

    suspend fun createTenant(tenant: Tenant): Boolean

    suspend fun getTenant(tenantId: String): DocumentSnapshot?

    suspend fun updateTenant(tenantId: String, tenantHashMap: HashMap<String, Any>): Boolean

    suspend fun deleteTenant(tenantId: String): Boolean

    suspend fun getHomeDetails(homeId: String): DocumentSnapshot?

    suspend fun getAllHomes(): List<DocumentSnapshot?>

    suspend fun addToViewedHomes(home: Home): Boolean

    suspend fun addToFavouriteHomes(home: Home): Boolean

//    suspend fun getRecommendedHomes(): List<DocumentSnapshot?>
}