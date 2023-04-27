package com.omouravictor.ratesbr.presenter.stocks.model

import java.util.*

data class StockUiModel(
    val name: String,
    val fullName: String,
    val location: String,
    val points: Double,
    val variation: Double,
    val stockDate: Date
)