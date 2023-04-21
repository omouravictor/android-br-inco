package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName

data class NetworkBitCoinsResultsResponse(
    @SerializedName("bitcoin")
    val bitcoins: LinkedHashMap<String, NetworkBitCoinsResultsItemResponse>
)
