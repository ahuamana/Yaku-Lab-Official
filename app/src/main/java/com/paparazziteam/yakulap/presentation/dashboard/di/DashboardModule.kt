package com.paparazziteam.yakulap.presentation.dashboard.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.paparazziteam.yakulap.presentation.dashboard.model.ChallengeRepository
import com.paparazziteam.yakulap.presentation.dashboard.model.ChallengeRepositoryImpl
import com.paparazziteam.yakulap.presentation.dashboard.model.CommentRepository
import com.paparazziteam.yakulap.presentation.dashboard.model.CommentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    //Collection Laboratory Digital
    @LaboratoryCollection
    @Provides
    @Singleton
    fun provideLaboratoryCollection(
        firestore: FirebaseFirestore
    ):CollectionReference{
        return firestore.collection("LaboratorioDigital")
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LaboratoryCollection

    //Collection comment
    @CommentCollection
    @Provides
    @Singleton
    fun provideCommentCollection(
        firestore: FirebaseFirestore
    ):CollectionReference{
        return firestore.collection("Comment")
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CommentCollection


    //Provide Interfaces Liskow Sustitución (Challenge Interface)
    @Provides
    @Singleton
    fun provideChallengeProvider(
        @DashboardModule.LaboratoryCollection collection:CollectionReference): ChallengeRepository {
        return ChallengeRepositoryImpl(collection)
    }

    //Provide Interfaces Liskow Sustitución (Comment Interface)
    @Provides
    @Singleton
    fun provideCommentProvider(
        @DashboardModule.CommentCollection collection:CollectionReference): CommentRepository {
        return CommentRepositoryImpl(collection)
    }

}