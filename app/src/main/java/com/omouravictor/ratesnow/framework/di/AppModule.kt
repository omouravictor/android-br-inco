package com.omouravictor.ratesnow.framework.di

import android.content.Context
import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.network.ApiService
import com.omouravictor.ratesnow.data.repository.BitCoinRepository
import com.omouravictor.ratesnow.data.repository.RatesApiRepository
import com.omouravictor.ratesnow.data.repository.RatesLocalRepository
import com.omouravictor.ratesnow.data.repository.StocksRepository
import com.omouravictor.ratesnow.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDataBase =
        AppDataBase.getDatabase(appContext)

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

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}