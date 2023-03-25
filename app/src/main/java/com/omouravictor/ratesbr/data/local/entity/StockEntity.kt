package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import java.util.*

@Entity(tableName = "stock_table")
data class StockEntity(
    @PrimaryKey(autoGenerate = false)
    val stockTerm: String,
    val stockName: String,
    val stockLocation: String,
    val stockPoints: Double,
    val stockVariation: Double,
    val date: Date,
)

fun StockEntity.toStockUiModel() =
    StockUiModel(
        stockTerm,
        stockLocation,
        stockVariation,
        date
    )