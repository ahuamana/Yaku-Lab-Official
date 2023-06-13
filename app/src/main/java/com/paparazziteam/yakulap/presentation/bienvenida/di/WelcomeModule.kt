package com.paparazziteam.yakulap.presentation.bienvenida.di

import android.app.Application
import com.paparazziteam.yakulap.presentation.bienvenida.model.IntroRepository
import com.paparazziteam.yakulap.presentation.bienvenida.model.IntroRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WelcomeModule {

    //Donde necesite un IntroRespitory debe devolver siempre un IntroRepositoryImpl
    @Provides
    @Singleton
    fun provideIntroImpl(application: Application): IntroRepository {
        return IntroRepositoryImpl(application)
    }
}