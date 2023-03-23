package com.omouravictor.ratesnow.framework.di

import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.network.ApiService
import com.omouravictor.ratesnow.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideRatesApiRepository(api: ApiService): RatesApiRepository =
        RatesApiRepository(api)

    @Singleton
    @Provides
    fun provideRatesLocalRepository(dataBase: AppDataBase): RatesLocalRepository =
        RatesLocalRepository(dataBase)

    @Singleton
    @Provides
    fun provideBitCoinsApiRepository(api: ApiService): BitCoinsApiRepository =
        BitCoinsApiRepository(api)

    @Singleton
    @Provides
    fun provideBitCoinsLocalRepository(dataBase: AppDataBase): BitCoinsLocalRepository =
        BitCoinsLocalRepository(dataBase)

    @Singleton
    @Provides
    fun provideStocksApiRepository(api: ApiService): StocksApiRepository =
        StocksApiRepository(api)

    @Singleton
    @Provides
    fun provideStocksLocalRepository(dataBase: AppDataBase): StocksLocalRepository =
        StocksLocalRepository(dataBase)

}