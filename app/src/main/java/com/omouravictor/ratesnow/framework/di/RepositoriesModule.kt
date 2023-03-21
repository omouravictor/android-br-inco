package com.omouravictor.ratesnow.framework.di

import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.network.ApiService
import com.omouravictor.ratesnow.data.repository.BitCoinRepository
import com.omouravictor.ratesnow.data.repository.RatesApiRepository
import com.omouravictor.ratesnow.data.repository.RatesLocalRepository
import com.omouravictor.ratesnow.data.repository.StocksRepository
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
    fun provideStocksRepository(api: ApiService, dataBase: AppDataBase): StocksRepository =
        StocksRepository(api, dataBase)

    @Singleton
    @Provides
    fun provideBitCoinsRepository(api: ApiService, dataBase: AppDataBase): BitCoinRepository =
        BitCoinRepository(api, dataBase)
}