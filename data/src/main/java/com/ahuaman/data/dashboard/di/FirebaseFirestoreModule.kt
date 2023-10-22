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

    @Singleton
    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore {
        val db = FirebaseFirestore.getInstance()

        val localChacheSettings = FirebaseFirestoreSettings
            .Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            .build()

        db.firestoreSettings = localChacheSettings

        return db
    }
}