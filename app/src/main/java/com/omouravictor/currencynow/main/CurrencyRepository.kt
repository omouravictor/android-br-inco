package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.CurrencyApi
import com.omouravictor.currencynow.data.models.CurrencyApiResponse
import com.omouravictor.currencynow.database.AppDataBase
import com.omouravictor.currencynow.database.entity.RatesEntity
import com.omouravictor.currencynow.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val API_KEY = "rtjnuP6eoDYLeuJXl0u3SiqB11ybal6h"

class CurrencyApiRepository @Inject constructor(
    private val api: CurrencyApi, private val database: AppDataBase
) : MainRepository {

    override suspend fun getRatesFromApi(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<CurrencyApiResponse> {
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