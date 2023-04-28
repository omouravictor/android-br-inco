package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
import com.omouravictor.ratesbr.util.StringUtils.getCityInPortuguese
import com.omouravictor.ratesbr.util.StringUtils.getCountryInPortuguese
import java.util.*

@Entity(tableName = "stock_table")
data class StockEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val fullName: String,
    val fullLocation: String,
    val points: Double,
    val variation: Double,
    val stockDate: Date,
)

fun StockEntity.toStockUiModel(): StockUiModel {
    val fullLocationList = fullLocation.split(", ")
    val country = fullLocationList.last()
    val city = fullLocationList.first()

    return StockUiModel(
        name,
        fullName,
        getCountryInPortuguese(country),
        getCityInPortuguese(city),
        points,
        variation,
        stockDate
    )
}