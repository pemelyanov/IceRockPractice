package com.emelyanov.icerockpractice.shared.di

import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ModuleCore {

    @Singleton
    @Provides
    fun provideCoreNavProvider(): CoreNavProvider = CoreNavProvider()
}