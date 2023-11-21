package com.omouravictor.br_inco.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.br_inco.presenter.rates.model.RateUiModel
import com.omouravictor.br_inco.util.StringUtils.getCurrencyNameInPortuguese
import java.util.Date

@Entity(tableName = "rate_table")
data class RateEntity(
    @PrimaryKey(autoGenerate = false)
    val currencyTerm: String,
    val unitaryRate: Double,
    val variation: Double,
    val rateDate: Date
)

fun RateEntity.toRateUiModel() = RateUiModel(
    getCurrencyNameInPortuguese(currencyTerm),
    currencyTerm,
    unitaryRate,
    variation,
    rateDate
)