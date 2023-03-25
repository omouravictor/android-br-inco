package com.omouravictor.ratesbr.presenter.bitcoins.model

import java.util.*

data class BitCoinUiModel(
    val bitcoinName: String,
    val bitcoinISO: String,
    val bitcoinLanguage: String,
    val bitcoinLast: Double,
    val bitcoinVariation: Double,
    val date: Date
)