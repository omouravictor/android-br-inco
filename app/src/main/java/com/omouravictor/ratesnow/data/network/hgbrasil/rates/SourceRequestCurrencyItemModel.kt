package com.omouravictor.ratesnow.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName

data class SourceRequestCurrencyItemModel(
    @SerializedName("name")
    val requestCurrencyName: String,

    @SerializedName("buy")
    val requestCurrencyBuy: Double,

    @SerializedName("sell")
    val requestCurrencySell: Double,

    @SerializedName("variation")
    val requestCurrencyVariation: Double
)
