package com.omouravictor.ratesnow.main

import com.omouravictor.ratesnow.data.RatesApi
import com.omouravictor.ratesnow.data.models.ApiResponse
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.RatesEntity
import com.omouravictor.ratesnow.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val API_KEY = "rtjnuP6eoDYLeuJXl0u3SiqB11ybal6h"

class RatesRepository @Inject constructor(
    private val api: RatesApi, private val database: AppDataBase
) : MainRepository {

    override suspend fun getRatesFromApi(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<ApiResponse> {
        return try {
            val response = api.getRates(fromCurrency, toCurrencies, API_KEY)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun getAllRatesFromDb(): Flow<List<RatesEntity>> {
        return database.rateDao().getAllRates()
    }

    override fun getRatesFromDb(currencyBase: String): RatesEntity {
        return database.rateDao().getRatesForCurrency(currencyBase)
    }

    override suspend fun insertRatesOnDb(rates: RatesEntity) {
        database.rateDao().insertRate(rates)
    }

    override suspend fun removeAllRates() {
        database.rateDao().deleteAllRates()
    }
}