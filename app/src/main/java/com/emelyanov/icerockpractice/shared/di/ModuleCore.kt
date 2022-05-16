package com.emelyanov.icerockpractice.shared.di

import android.content.Context
import com.emelyanov.icerockpractice.navigation.core.CoreNavProvider
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.AppRepository
import com.emelyanov.icerockpractice.shared.domain.services.apprepository.IAppRepository
import com.emelyanov.icerockpractice.shared.domain.services.githubapi.IGitHubApi
import com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage.IKeyValueStorage
import com.emelyanov.icerockpractice.shared.domain.services.keyvaluestorage.KeyValueStorage
import com.emelyanov.icerockpractice.shared.domain.usecases.GetAuthHeaderUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@ExperimentalSerializationApi
@InstallIn(SingletonComponent::class)
@Module
class ModuleCore {

    @Singleton
    @Provides
    fun provideCoreNavProvider(): CoreNavProvider = CoreNavProvider()

    private val json = Json { ignoreUnknownKeys = true }
    private val mediaType = MediaType.get("application/json; charset=utf-8")
    @Singleton
    @Provides
    fun provideGitApi(): IGitHubApi
    = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(json.asConverterFactory(mediaType))
        .build()
        .create(IGitHubApi::class.java)

    @Singleton
    @Provides
    fun provideKeyValueStorage(@ApplicationContext context: Context): IKeyValueStorage = KeyValueStorage(context)

    @Singleton
    @Provides
    fun provideAppRepository(
        gitHubApi: IGitHubApi,
        getAuthHeaderUseCase: GetAuthHeaderUseCase,
        keyValueStorage: IKeyValueStorage
    ): IAppRepository
    = AppRepository(
        gitHubApi = gitHubApi,
        getAuthHeader = getAuthHeaderUseCase,
        keyValueStorage = keyValueStorage
    )
}