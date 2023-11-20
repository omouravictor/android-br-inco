package com.omouravictor.brinco.data.network.hgfinanceapi.bitcoin

import com.google.gson.annotations.SerializedName

data class ApiBitcoinsResultsItemResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("format")
    val format: ArrayList<String>,

    @SerializedName("last")
    val last: Double,

    @SerializedName("variation")
    val variation: Double
)
