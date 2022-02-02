package com.denisgithuku.softkeja.feature_tenant.di

import com.denisgithuku.softkeja.feature_tenant.data.repository.TenantRepositoryImpl
import com.denisgithuku.softkeja.feature_tenant.domain.repository.TenantRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TenantModule {

    @Singleton
    @Provides
    fun provideFirebaseFirestoreInstance(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun providesTenantRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseCrashlytics: FirebaseCrashlytics
    ): TenantRepository = TenantRepositoryImpl(firebaseFirestore, firebaseCrashlytics)
}