package com.omouravictor.currencynow.data.models

data class Conversion(
    val fromCurrency: String,
    val toCurrency: String,
    val value: Double
)
