package com.omouravictor.ratesbr.presenter.bitcoins.model

import java.util.*

data class BitcoinUiModel(
    val name: String,
    val iSO: String,
    val language: String,
    val last: Double,
    val variation: Double,
    val bitcoinDate: Date
)