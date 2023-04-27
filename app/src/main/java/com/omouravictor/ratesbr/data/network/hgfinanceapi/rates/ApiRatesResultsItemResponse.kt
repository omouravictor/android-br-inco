package com.omouravictor.ratesbr.data.network.hgfinanceapi.rates

import com.google.gson.annotations.SerializedName

data class ApiRatesResultsItemResponse(
    @SerializedName("buy")
    val buy: Double,

    @SerializedName("variation")
    val variation: Double
)
