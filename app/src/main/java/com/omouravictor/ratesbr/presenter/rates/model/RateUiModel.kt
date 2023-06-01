package com.omouravictor.ratesbr.presenter.rates.model

import java.util.Date

data class RateUiModel(
    val currencyName: String,
    val currencyTerm: String,
    val unitaryRate: Double,
    val variation: Double,
    val rateDate: Date
)
