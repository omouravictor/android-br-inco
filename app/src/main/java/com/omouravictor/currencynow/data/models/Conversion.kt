package com.omouravictor.currencynow.data.models

import kotlin.math.round

data class Conversion(
    val fromCurrency: String,
    val toCurrency: String,
    var amount: Float,
    var rate: Double
) {
    fun getValue() = round(amount * rate * 100) / 100
}