package com.omouravictor.brinco.data.network.hgfinanceapi.bitcoin

import com.google.gson.annotations.SerializedName

data class ApiBitcoinsResultsResponse(
    @SerializedName("bitcoin")
    val bitcoins: LinkedHashMap<String, ApiBitcoinsResultsItemResponse>
)
