package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.CurrencyApi
import com.omouravictor.currencynow.data.models.CurrencyResponse
import com.omouravictor.currencynow.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {

    override suspend fun getRates(
        fromCurrency: String,
        toCurrencies: String,
        apikey: String
    ): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(fromCurrency, toCurrencies, apikey)
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
}