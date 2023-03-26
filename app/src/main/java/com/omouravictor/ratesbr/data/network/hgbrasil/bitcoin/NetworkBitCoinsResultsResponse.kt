package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName

data class NetworkBitCoinsResultsResponse(
    @SerializedName("bitcoin")
    val resultsBitcoin: LinkedHashMap<String, NetworkBitCoinsResultsItemResponse>
) {}
