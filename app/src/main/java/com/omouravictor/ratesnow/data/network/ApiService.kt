package com.omouravictor.ratesnow.data.network

import com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin.SourceRequestBitCoinModel
import com.omouravictor.ratesnow.data.network.hgbrasil.rates.SourceRequestCurrencyModel
import com.omouravictor.ratesnow.data.network.hgbrasil.stock.SourceRequestStockModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("finance")
    suspend fun getRates(
        @Query("fields") field: String
    ): SourceRequestCurrencyModel

    @GET("finance")
    suspend fun getBitCoins(
        @Query("fields") field: String
    ): SourceRequestBitCoinModel

    @GET("finance")
    suspend fun getStocks(
        @Query("fields") field: String
    ): SourceRequestStockModel
}