package com.omouravictor.ratesnow.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stock_table")
data class StockEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "stock_term")
    val stockTerm: String,

    @ColumnInfo(name = "stock_name")
    val stockName: String,

    @ColumnInfo(name = "stock_location")
    val stockLocation: String,

    @ColumnInfo(name = "stock_points")
    val stockPoints: Double,

    @ColumnInfo(name = "stock_variation")
    val stockVariation: Double,

    @ColumnInfo(name = "stock_date")
    val date: Date,
)
