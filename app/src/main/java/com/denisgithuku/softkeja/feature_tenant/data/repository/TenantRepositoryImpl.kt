package com.denisgithuku.softkeja.feature_tenant.data.repository

import com.denisgithuku.softkeja.feature_landlord.LandlordConstants
import com.denisgithuku.softkeja.feature_landlord.domain.model.Home
import com.denisgithuku.softkeja.feature_tenant.TenantsConstants
import com.denisgithuku.softkeja.feature_tenant.domain.model.Tenant
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class TenantRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseCrashlytics: FirebaseCrashlytics
): TenantRepository {

    override suspend fun createTenant(tenant: Tenant): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.TenantCollection)
                .document(tenant.idNo)
                .set(tenant, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
            false
        }
    }

    override suspend fun getTenant(tenantId: String): DocumentSnapshot? {
        return try {
            val doc = firebaseFirestore
                .collection(TenantsConstants.TenantCollection)
                .document(tenantId)
                .get()
                .await()
            doc
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
            null
        }
    }

    override suspend fun updateTenant(tenantId: String, tenantHashMap: HashMap<String, Any>): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.TenantCollection)
                .document(tenantId)
                .set(tenantHashMap, SetOptions.merge())
                .await()
            true
        }catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
            false
        }
    }

    override suspend fun deleteTenant(tenantId: String): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.TenantCollection)
                .document(tenantId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
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
            doc
        }catch (e: Exception) {
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
            null
        }
    }

    override suspend fun getAllHomes(): List<DocumentSnapshot?> {
        return try {
            val docs = firebaseFirestore
                .collection(LandlordConstants.homeCollection)
                .get()
                .await()
                .documents
            docs
        }catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addToViewedHomes(home: Home): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.viewHomes)
                .document()
                .set(home)
                .await()
            true
        }catch (e: Exception){
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
            false
        }
    }

    override suspend fun addToFavouriteHomes(home: Home): Boolean {
        return try {
            firebaseFirestore
                .collection(TenantsConstants.favouriteHomes)
                .document()
                .set(home)
                .await()
            true
        }catch (e: Exception){
            firebaseCrashlytics.setCustomKey(TenantsConstants.tenantRepoException, e.message.toString())
            false
        }
    }
}