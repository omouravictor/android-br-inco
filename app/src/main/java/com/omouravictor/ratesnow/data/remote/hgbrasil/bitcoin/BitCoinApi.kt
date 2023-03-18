package com.omouravictor.ratesnow.data.remote.hgbrasil.bitcoin

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BitCoinApi {

    @GET("finance")
    suspend fun getBitCoin(
        @Query("fields") field: String,
        @Query("key") apikey: String
    ): Response<SourceRequestBitcoinModel>
}