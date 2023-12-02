package com.omouravictor.br_inco.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.br_inco.presenter.stocks.model.StockUiModel
import com.omouravictor.br_inco.util.StringUtils
import java.util.Date

@Entity(tableName = "stock_table")
data class StockEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val fullName: String,
    val countryLocation: String,
    val cityLocation: String,
    val points: Double,
    val variation: Double,
    val stockDate: Date,
)

fun StockEntity.toStockUiModel() = StockUiModel(
    name,
    fullName,
    StringUtils.getCountryInPortuguese(countryLocation),
    StringUtils.getCityInPortuguese(cityLocation),
    points,
    variation,
    stockDate
)