package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.stocks.model.StockUiModel
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
        name = name,
        fullName = fullName,
        countryLocation = when (country) {
            "Brazil" -> "Brasil"
            "United States" -> "Estados Unidos"
            "French" -> "França"
            "Japan" -> "Japão"
            else -> country
        },
        cityLocation = when (city) {
            "Sao Paulo" -> "São Paulo"
            "New York City" -> "Nova Iorque"
            "Paris" -> "Paris"
            "Tokyo" -> "Tóquio"
            else -> city
        },
        points = points,
        variation = variation,
        stockDate = stockDate
    )
}