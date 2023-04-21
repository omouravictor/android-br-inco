package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName

data class NetworkBitCoinsResultsItemResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("format")
    val format: ArrayList<String>,

    @SerializedName("last")
    val last: Double,

    @SerializedName("variation")
    val variation: Double
)
