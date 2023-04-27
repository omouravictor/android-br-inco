package com.omouravictor.ratesbr.data.network.hgfinanceapi.stock

import com.google.gson.annotations.SerializedName

data class ApiStocksResultsResponse(
    @SerializedName("stocks")
    val stocks: LinkedHashMap<String, ApiStocksResultsItemResponse>
)
