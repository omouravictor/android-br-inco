package com.omouravictor.ratesnow.api.hgbrasil

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StocksApi {

    @GET("finance")
    suspend fun getStocks(
        @Query("fields") field: String,
        @Query("key") apikey: String
    ): Response<SourceRequestStockModel>
}