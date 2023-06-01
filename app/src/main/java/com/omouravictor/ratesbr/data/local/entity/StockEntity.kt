package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.StringUtils.getCityInPortuguese
import com.omouravictor.ratesbr.util.StringUtils.getCountryInPortuguese
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
    getCountryInPortuguese(countryLocation),
    getCityInPortuguese(cityLocation),
    points,
    variation,
    stockDate
)