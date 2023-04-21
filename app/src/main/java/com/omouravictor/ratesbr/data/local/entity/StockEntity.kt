package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import java.util.*

@Entity(tableName = "stock_table")
data class StockEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val location: String,
    val variation: Double,
    val date: Date,
)

fun StockEntity.toStockUiModel() =
    StockUiModel(
        name,
        location,
        variation,
        date
    )