package com.omouravictor.ratesbr.data.network.hgfinanceapi.rates

import com.google.gson.annotations.SerializedName

data class ApiRatesResultsResponse(
    @SerializedName("currencies")
    val currencies: LinkedHashMap<String, ApiRatesResultsItemResponse>,
)
