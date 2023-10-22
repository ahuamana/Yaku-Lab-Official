package com.ahuaman.data.dashboard.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseFirestoreModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore {
        val db = FirebaseFirestore.getInstance().also {
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            it.firestoreSettings = settings
        }
        return db
    }
}