package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName

data class NetworkBitcoinsResultsResponse(
    @SerializedName("bitcoin")
    val bitcoins: LinkedHashMap<String, NetworkBitcoinsResultsItemResponse>
)
