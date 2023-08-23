package com.ahuaman.data.dashboard.di

import com.ahuaman.data.dashboard.remote.FirebaseImageRemoteDataSource
import com.ahuaman.data.dashboard.remote.FirebaseImageService
import com.yakulab.framework.network.di.RetrofitFirebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseImageModule {



    @Provides
    @Singleton
    fun provideFirebaseImageService(@RetrofitFirebase retrofitFirebase: Retrofit) : FirebaseImageService = retrofitFirebase.create(
        FirebaseImageService::class.java
    )


    @Provides
    @Singleton
    fun provideFirebaseImageRemoteDataSource(firebaseImageService: FirebaseImageService) = FirebaseImageRemoteDataSource(firebaseImageService)
}