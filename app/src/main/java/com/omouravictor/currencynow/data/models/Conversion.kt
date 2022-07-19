package com.omouravictor.currencynow.data.models

import java.util.*
import kotlin.math.round

class Conversion(
    val fromCurrency: String,
    val toCurrency: String,
    var amount: Float,
    var rate: Double,
    var rateDate: Date
) {
    fun getValue() = round(amount * rate * 100) / 100
}