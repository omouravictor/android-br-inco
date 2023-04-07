package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import java.util.*

@Entity(tableName = "rate_table")
data class RateEntity(
    @PrimaryKey(autoGenerate = false)
    val currency: String,
    val unityRate: Double,
    val rateDate: Date
)

fun RateEntity.toRateUiModel() =
    RateUiModel(
        currency,
        unityRate,
        unityRate,
        rateDate
    )