package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.CurrencyApi
import com.omouravictor.currencynow.data.models.CurrencyResponse
import com.omouravictor.currencynow.util.Resource
import javax.inject.Inject

private const val TO_CURRENCIES = "USD%2C%20EUR%2C%20JPY%2C%20GBP%2C%20CAD%2C%20BRL"
private const val API_KEY = "u3J6BxflZCOOCm5w5MyRHP4bgT5B5CxH"

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {

    override suspend fun getRates(
        fromCurrency: String
    ): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(fromCurrency, TO_CURRENCIES, API_KEY)
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