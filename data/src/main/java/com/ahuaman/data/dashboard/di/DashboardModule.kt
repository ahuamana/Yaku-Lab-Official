package com.ahuaman.data.dashboard.di

import com.ahuaman.data.dashboard.remote.DashboardRemoteDataSource
import com.ahuaman.data.dashboard.remote.DashboardService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yakulab.framework.network.di.RetrofitInaturalist
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {


    @Provides
    @Singleton
    fun provideDashboardService(@RetrofitInaturalist retrofit: Retrofit) : DashboardService = retrofit.create(
        DashboardService::class.java
    )

    @Provides
    @Singleton
    fun provideDashboardRemoteDataSource(dashboardService: DashboardService) = DashboardRemoteDataSource(dashboardService)

    //Collections Users
    @UsersCollection
    @Provides
    @Singleton
    fun provideUsersCollection(
        firestore: FirebaseFirestore
    ): CollectionReference {
        return firestore.collection("Users")
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersCollection
}