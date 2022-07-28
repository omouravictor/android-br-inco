package com.omouravictor.ratesnow.api.hgbrasil

import com.google.gson.annotations.SerializedName

data class SourceRequestStockItemModel(
    @SerializedName("name")
    val requestStockName: String,

    @SerializedName("location")
    val requestStockLocation: String,

    @SerializedName("points")
    val requestStockPoints: Double,

    @SerializedName("variation")
    val requestStockVariation: Double
)
