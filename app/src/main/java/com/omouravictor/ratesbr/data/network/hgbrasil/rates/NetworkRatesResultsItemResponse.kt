package com.omouravictor.ratesbr.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName

data class NetworkRatesResultsItemResponse(
    @SerializedName("buy")
    val buy: Double,

    @SerializedName("variation")
    val variation: Double
)
