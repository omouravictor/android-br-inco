package com.omouravictor.ratesbr.presenter.bitcoins.model

import java.util.*

data class BitcoinUiModel(
    val name: String,
    val currencyName: String,
    val currencyTerm: String,
    val language: String,
    val countryLanguage: String,
    val unitaryRate: Double,
    val variation: Double,
    val bitcoinDate: Date
)