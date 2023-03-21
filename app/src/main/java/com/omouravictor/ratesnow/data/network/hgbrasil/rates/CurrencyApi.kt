package com.omouravictor.ratesnow.data.network.hgbrasil.rates

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("finance")
    suspend fun getCurrencies(
        @Query("fields") field: String
    ): SourceRequestCurrencyModel
}