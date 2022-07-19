package com.omouravictor.currencynow.data.models

data class CurrencyApiResponse(
    val base: String,
    val date: String,
    val rates: RatesApiResponse
)