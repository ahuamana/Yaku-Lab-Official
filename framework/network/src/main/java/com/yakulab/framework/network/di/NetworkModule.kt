package com.yakulab.framework.network.di

import com.paparazziteam.yakulab.binding.Constants.BASE_GITHUB_IO_URL
import com.paparazziteam.yakulab.binding.Constants.BASE_URL_FIREBASE_IMAGES
import com.paparazziteam.yakulab.binding.Constants.BASE_URL_INATURALIST
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @RetrofitInaturalist
    @Provides
    @Singleton
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_INATURALIST)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @RetrofitFirebase
    @Provides
    @Singleton
    fun provideFirebaseImageRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_FIREBASE_IMAGES)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @RetrofitGithubIO
    @Provides
    @Singleton
    fun provideGithubIORetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_GITHUB_IO_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }



    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitInaturalist

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitFirebase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitGithubIO