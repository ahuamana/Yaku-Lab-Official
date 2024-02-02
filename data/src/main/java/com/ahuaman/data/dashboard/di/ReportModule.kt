package com.ahuaman.data.dashboard.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportModule {

    //Collections Users
    @ReportsCollection
    @Provides
    @Singleton
    fun provideReportCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection("Reports")
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ReportsCollection
}