package com.omouravictor.ratesbr.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName

data class NetworkRatesResultsResponse(
    @SerializedName("currencies")
    val resultsCurrencies: LinkedHashMap<String, NetworkRatesResultsItemResponse>,
)
