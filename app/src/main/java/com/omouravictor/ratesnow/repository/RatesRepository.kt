package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.api.apilayer.RatesApi
import com.omouravictor.ratesnow.api.apilayer.SourceRequestRatesModel
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.RatesEntity
import com.omouravictor.ratesnow.util.Resource
import javax.inject.Inject

private const val API_KEY = "1vIuWTLu5sWKv9H0qbFurrWs5OVnZT68"

class RatesRepository @Inject constructor(
    private val api: RatesApi, private val database: AppDataBase
) {

    suspend fun getRatesFromApi(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<SourceRequestRatesModel> {
        return try {
            val response = api.getRates(fromCurrency, toCurrencies, API_KEY)
            val result = response.body()
            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else
                Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    fun getRatesFromDb(currencyBase: String): RatesEntity? {
        return database.rateDao().getRatesForCurrency(currencyBase)
    }

    fun insertRatesOnDb(rates: RatesEntity) {
        database.rateDao().insertRate(rates)
    }
}