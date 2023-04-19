package com.omouravictor.ratesbr.presenter.rates.model

import java.util.*

data class RateUiModel(
    val currencyName: String,
    val currencyTerm: String,
    val unityRate: Double,
    val variation: Double,
    val rateDate: Date
)
