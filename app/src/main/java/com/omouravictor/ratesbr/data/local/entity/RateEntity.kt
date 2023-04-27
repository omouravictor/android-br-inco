package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.util.*

@Entity(tableName = "rate_table")
data class RateEntity(
    @PrimaryKey(autoGenerate = false)
    val currencyTerm: String,
    val unitaryRate: Double,
    val variation: Double,
    val rateDate: Date
)

fun RateEntity.toRateUiModel() =
    RateUiModel(
        currencyName = when (currencyTerm) {
            "USD" -> "Dólar americano"
            "EUR" -> "Euro"
            "GBP" -> "Libra esterlina"
            "ARS" -> "Peso argentino"
            "CAD" -> "Dólar canadense"
            "AUD" -> "Dólar australiano"
            "JPY" -> "Iene japonês"
            "CNY" -> "Yuan chinês"
            else -> "Moeda não encontrada"
        },
        currencyTerm = currencyTerm,
        unitaryRate = unitaryRate,
        variation = variation,
        rateDate = rateDate
    )