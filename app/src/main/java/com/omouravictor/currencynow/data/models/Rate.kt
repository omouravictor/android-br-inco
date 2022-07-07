package com.omouravictor.currencynow.data.models

data class Rate(
    val fromCurrency: String,
    val toCurrency: String,
    val value: Double
)
