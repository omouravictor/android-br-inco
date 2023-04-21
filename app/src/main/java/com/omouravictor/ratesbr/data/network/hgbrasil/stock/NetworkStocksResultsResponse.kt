package com.omouravictor.ratesbr.data.network.hgbrasil.stock

import com.google.gson.annotations.SerializedName

data class NetworkStocksResultsResponse(
    @SerializedName("stocks")
    val stocks: LinkedHashMap<String, NetworkStocksResultsItemResponse>
)
