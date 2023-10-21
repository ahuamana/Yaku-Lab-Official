package com.paparazziteam.yakulap.di

import com.paparazziteam.yakulab.binding.helper.navigator.Navigator
import com.paparazziteam.yakulap.navigation.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator {
        return NavigatorImpl()
    }
}