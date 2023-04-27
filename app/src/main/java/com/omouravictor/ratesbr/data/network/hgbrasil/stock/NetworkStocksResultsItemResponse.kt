package com.omouravictor.ratesbr.data.network.hgbrasil.stock

import com.google.gson.annotations.SerializedName

data class NetworkStocksResultsItemResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("points")
    val points: Double,

    @SerializedName("variation")
    val variation: Double
)
