package com.omouravictor.currencynow.data.models

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: RateValues
)