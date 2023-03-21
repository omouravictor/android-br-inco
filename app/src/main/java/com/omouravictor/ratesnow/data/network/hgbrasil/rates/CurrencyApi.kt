package com.omouravictor.ratesnow.data.network.hgbrasil.rates

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("finance")
    suspend fun getCurrencies(
        @Query("fields") field: String
//        @Query("key") key: String
    ): Response<SourceRequestCurrencyModel>
}