package com.omouravictor.brinco.data.network.hgfinanceapi.stock

import com.google.gson.annotations.SerializedName

data class ApiStocksResultsItemResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("points")
    val points: Double,

    @SerializedName("variation")
    val variation: Double
)
