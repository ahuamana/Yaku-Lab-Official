package com.paparazziteam.yakulap.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.paparazziteam.yakulab.binding.helper.analytics.FBaseAnalytics
import com.paparazziteam.yakulap.navigation.FBaseAnalyticsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FbaseAnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFBaseAnalytics(firebaseAnalytics: FirebaseAnalytics): FBaseAnalytics {
        return FBaseAnalyticsImpl(firebaseAnalytics)
    }

}