package com.omouravictor.currencynow.data.models

import com.google.gson.annotations.SerializedName

data class Rates(
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
    @SerializedName("AUD")
    val aUD: Double,
    @SerializedName("CHF")
    val cHF: Double,
    @SerializedName("ZND")
    val zND: Double,
    @SerializedName("BRL")
    val bRL: Double
)