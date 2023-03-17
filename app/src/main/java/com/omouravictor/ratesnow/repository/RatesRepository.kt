package com.omouravictor.ratesnow.repository

import com.omouravictor.ratesnow.api.apilayer.RatesApi
import com.omouravictor.ratesnow.api.apilayer.SourceRequestRatesModel
import com.omouravictor.ratesnow.database.AppDataBase
import com.omouravictor.ratesnow.database.entity.RatesEntity
import com.omouravictor.ratesnow.util.Resource
import javax.inject.Inject

private const val API_KEY = "vun27fVe86DqCJOVkaomPgQNItQg6sus"

class RatesRepository @Inject constructor(
    private val api: RatesApi, private val database: AppDataBase
) {
    suspend fun getAllFromApi(
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

    fun getFromDb(currencyBase: String): RatesEntity? {
        return database.rateDao().getRatesForCurrency(currencyBase)
    }

    fun insertOnDb(rates: RatesEntity) {
        database.rateDao().insertRate(rates)
    }
}