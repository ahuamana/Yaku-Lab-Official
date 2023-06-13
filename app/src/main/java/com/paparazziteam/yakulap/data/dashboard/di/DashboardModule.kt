package com.paparazziteam.yakulap.data.dashboard.di

import com.paparazziteam.yakulap.data.dashboard.remote.DashboardRemoteDataSource
import com.paparazziteam.yakulap.data.dashboard.remote.DashboardService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    @Provides
    @Singleton
    fun provideDashboardService(retrofit: Retrofit) : DashboardService = retrofit.create(
        DashboardService::class.java
    )

    @Provides
    @Singleton
    fun provideDashboardRemoteDataSource(dashboardService: DashboardService) = DashboardRemoteDataSource(dashboardService)

}