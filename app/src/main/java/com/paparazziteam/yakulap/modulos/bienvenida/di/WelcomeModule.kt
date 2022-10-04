package com.paparazziteam.yakulap.modulos.bienvenida.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.paparazziteam.yakulap.modulos.bienvenida.model.IntroRepository
import com.paparazziteam.yakulap.modulos.bienvenida.model.IntroRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
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