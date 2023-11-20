package com.omouravictor.brinco.framework.di

import com.omouravictor.brinco.data.local.AppDataBase
import com.omouravictor.brinco.data.network.hgfinanceapi.ApiService
import com.omouravictor.brinco.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideRatesRepository(
        dataBase: AppDataBase,
        apiService: ApiService
    ): RatesRepository = RatesRepository(dataBase.rateDao(), apiService)

    @Singleton
    @Provides
    fun provideBitcoinsRepository(
        dataBase: AppDataBase,
        apiService: ApiService
    ): BitcoinsRepository = BitcoinsRepository(dataBase.bitcoinDao(), apiService)

    @Singleton
    @Provides
    fun provideStocksRepository(
        dataBase: AppDataBase,
        apiService: ApiService
    ): StocksRepository = StocksRepository(dataBase.stockDao(), apiService)

}