package com.omouravictor.ratesnow.data.network.apilayer

import com.omouravictor.ratesnow.data.network.apilayer.model.RatesNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("/exchangerates_data/latest")
    suspend fun getRates(
        @Query("base") fromCurrency: String,
        @Query("symbols") toCurrencies: String,
    ): RatesNetworkResponse
}