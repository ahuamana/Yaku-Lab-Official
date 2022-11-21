package com.paparazziteam.yakulap.helper.di

import android.content.Context
import com.paparazziteam.yakulap.helper.application.CacheData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext appContext: Context) = CacheData()
}