package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.models.CurrencyApiResponse
import com.omouravictor.currencynow.database.entity.RatesEntity
import com.omouravictor.currencynow.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getAllRatesFromDb(): Flow<List<RatesEntity>>

    fun getRatesFromDb(currencyBase: String): RatesEntity?

    suspend fun getRatesFromApi(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<CurrencyApiResponse>

    suspend fun insertRatesOnDb(rates: RatesEntity)

    suspend fun removeAllRates()
}