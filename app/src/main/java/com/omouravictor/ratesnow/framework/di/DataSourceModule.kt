package com.omouravictor.ratesnow.framework.di

import com.omouravictor.ratesnow.data.network.apilayer.remote.RatesDataSource
import com.omouravictor.ratesnow.data.network.apilayer.remote.RetrofitRatesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface DataSourceModule {
    @Singleton
    @Binds
    fun bindRatesDataSource(
        retrofitRatesDataSource: RetrofitRatesDataSource
    ): RatesDataSource
}