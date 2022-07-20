package com.omouravictor.ratesnow.main

import com.omouravictor.ratesnow.data.models.ApiResponse
import com.omouravictor.ratesnow.database.entity.RatesEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getAllRatesFromDb(): Flow<List<RatesEntity>>

    fun getRatesFromDb(currencyBase: String): RatesEntity?

    suspend fun getRatesFromApi(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<ApiResponse>

    suspend fun insertRatesOnDb(rates: RatesEntity)

    suspend fun removeAllRates()
}