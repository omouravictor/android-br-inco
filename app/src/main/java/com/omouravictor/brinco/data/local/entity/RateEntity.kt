package com.omouravictor.brinco.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.brinco.presenter.rates.model.RateUiModel
import com.omouravictor.brinco.util.StringUtils.getCurrencyNameInPortuguese
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