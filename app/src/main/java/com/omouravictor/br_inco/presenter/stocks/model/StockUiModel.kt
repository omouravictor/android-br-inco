package com.omouravictor.br_inco.presenter.stocks.model

import java.util.Date

data class StockUiModel(
    val name: String,
    val fullName: String,
    val countryLocation: String,
    val cityLocation: String,
    val points: Double,
    val variation: Double,
    val stockDate: Date
)