package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.models.CurrencyResponse
import com.omouravictor.currencynow.util.Resource

interface MainRepository {

    suspend fun getRates(
        fromCurrency: String,
        toCurrencies: String,
        apikey: String
    ): Resource<CurrencyResponse>
}