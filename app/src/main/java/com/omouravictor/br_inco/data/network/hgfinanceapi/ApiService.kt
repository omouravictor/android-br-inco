package com.omouravictor.br_inco.data.network.hgfinanceapi

import com.omouravictor.br_inco.data.network.hgfinanceapi.bitcoin.ApiBitcoinsResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.rates.ApiRatesResponse
import com.omouravictor.br_inco.data.network.hgfinanceapi.stock.ApiStocksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("finance")
    suspend fun getRates(
        @Query("fields") field: String
    ): ApiRatesResponse

    @GET("finance")
    suspend fun getBitcoins(
        @Query("fields") field: String
    ): ApiBitcoinsResponse

    @GET("finance")
    suspend fun getStocks(
        @Query("fields") field: String
    ): ApiStocksResponse
}