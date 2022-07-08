package com.omouravictor.currencynow.data

import com.omouravictor.currencynow.data.models.CurrencyApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/exchangerates_data/latest")
    suspend fun getRates(
        @Query("base") fromCurrency: String,
        @Query("symbols") toCurrencies: String,
        @Query("apikey") apikey: String
    ): Response<CurrencyApiResponse>
}