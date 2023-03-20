package com.omouravictor.ratesnow.framework.di

import com.omouravictor.ratesnow.domain.usecases.GetRates
import com.omouravictor.ratesnow.domain.usecases.GetRatesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface UseCasesModule {
    @Singleton
    @Binds
    fun bindGetRatesUseCase(
        getRates: GetRates
    ): GetRatesUseCase
}