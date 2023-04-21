package com.omouravictor.ratesbr.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName

data class NetworkRatesResultsResponse(
    @SerializedName("currencies")
    val currencies: LinkedHashMap<String, NetworkRatesResultsItemResponse>,
)
