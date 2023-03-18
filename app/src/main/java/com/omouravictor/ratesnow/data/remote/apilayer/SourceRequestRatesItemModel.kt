package com.omouravictor.ratesnow.data.remote.apilayer

import com.google.gson.annotations.SerializedName

data class SourceRequestRatesItemModel(
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