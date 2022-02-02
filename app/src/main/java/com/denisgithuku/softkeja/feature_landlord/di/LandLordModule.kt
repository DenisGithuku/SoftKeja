package com.denisgithuku.softkeja.feature_landlord.di

import com.denisgithuku.softkeja.feature_landlord.data.repository.LandlordRepositoryImpl
import com.denisgithuku.softkeja.feature_landlord.domain.repository.LandlordRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LandLordModule {

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun provideLandlordRepository(
        firebaseCrashlytics: FirebaseCrashlytics,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): LandlordRepository = LandlordRepositoryImpl(
        firebaseFirestore, firebaseStorage, firebaseCrashlytics
    )

}