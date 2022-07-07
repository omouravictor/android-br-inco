package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.CurrencyApi
import com.omouravictor.currencynow.data.models.CurrencyResponse
import com.omouravictor.currencynow.util.Resource
import javax.inject.Inject

private const val API_KEY = "rtjnuP6eoDYLeuJXl0u3SiqB11ybal6h"

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {

    override suspend fun getRates(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<CurrencyResponse> {
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
}