package com.omouravictor.ratesnow.framework.di

import com.omouravictor.ratesnow.data.repository.RatesRepository
import com.omouravictor.ratesnow.data.repository.RatesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface RepositoriesModule {
    @Singleton
    @Binds
    fun bindRatesRepository(
        ratesRepositoryImpl: RatesRepositoryImpl
    ): RatesRepository
}