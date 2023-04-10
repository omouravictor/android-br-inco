package com.omouravictor.ratesbr.presenter.stocks.model

import java.util.*

data class StockUiModel(
    val stockTerm: String,
    val stockLocation: String,
    val stockVariation: Double,
    val date: Date
)