package com.omouravictor.ratesnow.presenter.rates.model

import java.util.*

data class RateUiModel(
    val fromCurrency: String,
    val toCurrency: String,
    val rateDate: Date,
    val rate: Double
)
