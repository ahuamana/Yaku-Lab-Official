package com.ahuaman.data.dashboard.di

import com.ahuaman.data.dashboard.remote.ar.ARService
import com.yakulab.framework.network.di.RetrofitGithubIO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ARModule {

    @Provides
    @Singleton
    fun provideARService(@RetrofitGithubIO retrofit: Retrofit) : ARService = retrofit.create(
        ARService::class.java
    )



}