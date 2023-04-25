package com.omouravictor.ratesbr.presenter.stocks.model

import java.util.*

data class StockUiModel(
    val name: String,
    val location: String,
    val variation: Double,
    val stockDate: Date
)