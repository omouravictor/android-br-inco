package com.omouravictor.ratesnow.data.local.entity

import androidx.room.Entity
import com.omouravictor.ratesnow.presenter.rates.model.RateUiModel
import java.util.*

@Entity(tableName = "rate_table", primaryKeys = ["fromCurrency", "toCurrency"])
data class RateEntity(
    val fromCurrency: String,
    val toCurrency: String,
    val unityRate: Double,
    val rateDate: Date
)

fun RateEntity.toRateUiModel() =
    RateUiModel(
        fromCurrency,
        toCurrency,
        unityRate,
        unityRate,
        rateDate
    )