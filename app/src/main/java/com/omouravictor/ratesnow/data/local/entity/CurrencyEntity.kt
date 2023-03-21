package com.omouravictor.ratesnow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "rate_table")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = false)
    val currencyName: String,
    val rate: Double,
    val date: Date
)