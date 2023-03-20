package com.omouravictor.ratesnow.presenter.rates

import java.util.*

data class RatesDto(
    val fromCurrency: String,
    val toCurrency: String,
    var amount: Float,
    val rate: Double,
    val rateDate: Date
)