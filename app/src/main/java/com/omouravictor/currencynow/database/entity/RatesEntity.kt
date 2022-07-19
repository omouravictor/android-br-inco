package com.omouravictor.currencynow.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "rate_table")
data class RatesEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "currency_base")
    val currencyBase: String,
    @ColumnInfo(name = "USD")
    val uSD: Double,
    @ColumnInfo(name = "EUR")
    val eUR: Double,
    @ColumnInfo(name = "JPY")
    val jPY: Double,
    @ColumnInfo(name = "GBP")
    val gBP: Double,
    @ColumnInfo(name = "CAD")
    val cAD: Double,
    @ColumnInfo(name = "BRL")
    val bRL: Double,
    @ColumnInfo(name = "date")
    val date: Date,
)
