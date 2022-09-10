package com.denisgithuku.softkeja.di

import android.content.Context
import com.denisgithuku.softkeja.data.repository.HomeRepositoryImpl
import com.denisgithuku.softkeja.data.repository.UserRepositoryImpl
import com.denisgithuku.softkeja.domain.repository.HomeRepository
import com.denisgithuku.softkeja.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth): UserRepository = UserRepositoryImpl(firestore,firebaseAuth)

    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage

    @Singleton
    @Provides
    fun provideHomeRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): HomeRepository = HomeRepositoryImpl(firestore, storage)
}
