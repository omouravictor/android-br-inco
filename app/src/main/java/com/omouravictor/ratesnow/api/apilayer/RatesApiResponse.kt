package com.omouravictor.ratesnow.api.apilayer

data class RatesApiResponse(
    val base: String,
    val date: String,
    val rates: SourceRequestRatesModel
)