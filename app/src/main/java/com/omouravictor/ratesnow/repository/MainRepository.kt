package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.data.models.ApiResponse
import com.omouravictor.ratesnow.database.entity.RatesEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getRatesFromApi(fromCurrency: String, toCurrencies: String): Resource<ApiResponse>

    fun getAllRatesFromDb(): Flow<List<RatesEntity>>

    fun getRatesFromDb(currencyBase: String): RatesEntity?

    fun insertRatesOnDb(rates: RatesEntity)

}