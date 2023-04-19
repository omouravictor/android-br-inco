package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.util.*

@Entity(tableName = "rate_table")
data class RateEntity(
    @PrimaryKey(autoGenerate = false)
    val currencyTerm: String,
    val unityRate: Double,
    val variation: Double,
    val rateDate: Date
)

fun RateEntity.toRateUiModel() =
    RateUiModel(
        currencyName = when (currencyTerm) {
            "USD" -> "Dólar Americano"
            "EUR" -> "Euro"
            "GBP" -> "Libra Esterlina"
            "ARS" -> "Peso Argentino"
            "CAD" -> "Dólar Canadense"
            "AUD" -> "Dólar Australiano"
            "JPY" -> "Iene Japonês"
            "CNY" -> "Yuan Chinês"
            else -> "Moeda não encontrada"
        },
        currencyTerm = currencyTerm,
        unityRate = unityRate,
        variation = variation,
        rateDate = rateDate
    )