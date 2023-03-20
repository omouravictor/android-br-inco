package com.omouravictor.ratesnow.framework.di

import android.content.Context
import com.omouravictor.ratesnow.BuildConfig
import com.omouravictor.ratesnow.data.local.AppDataBase
import com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin.BitCoinApi
import com.omouravictor.ratesnow.data.network.hgbrasil.stock.StocksApi
import com.omouravictor.ratesnow.data.repository.BitCoinRepository
import com.omouravictor.ratesnow.data.repository.StocksRepository
import com.omouravictor.ratesnow.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStocksApi(): StocksApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_HG)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(StocksApi::class.java)

    @Singleton
    @Provides
    fun provideBitCoinsApi(): BitCoinApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_HG)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BitCoinApi::class.java)

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDataBase =
        AppDataBase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideStocksRepository(api: StocksApi, dataBase: AppDataBase): StocksRepository =
        StocksRepository(api, dataBase)

    @Singleton
    @Provides
    fun provideBitCoinsRepository(api: BitCoinApi, dataBase: AppDataBase): BitCoinRepository =
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