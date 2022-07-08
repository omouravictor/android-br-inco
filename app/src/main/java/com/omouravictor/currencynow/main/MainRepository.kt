package com.omouravictor.currencynow.main

import com.omouravictor.currencynow.data.models.CurrencyApiResponse
import com.omouravictor.currencynow.util.Resource

interface MainRepository {

    suspend fun getRates(
        fromCurrency: String,
        toCurrencies: String
    ): Resource<CurrencyApiResponse>
}