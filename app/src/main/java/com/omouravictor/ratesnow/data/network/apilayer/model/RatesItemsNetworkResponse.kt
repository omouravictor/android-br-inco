package com.omouravictor.ratesnow.data.network.apilayer.model

import com.google.gson.annotations.SerializedName

data class RatesItemsNetworkResponse(
    @SerializedName("USD")
    val uSD: Double,
    @SerializedName("EUR")
    val eUR: Double,
    @SerializedName("JPY")
    val jPY: Double,
    @SerializedName("GBP")
    val gBP: Double,
    @SerializedName("CAD")
    val cAD: Double,
    @SerializedName("BRL")
    val bRL: Double
)