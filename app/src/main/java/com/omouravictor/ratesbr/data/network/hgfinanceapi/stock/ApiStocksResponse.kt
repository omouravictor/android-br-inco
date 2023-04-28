package com.omouravictor.ratesbr.data.network.hgfinanceapi.stock

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.StockEntity
import java.util.*

data class ApiStocksResponse(
    @SerializedName("results")
    val results: ApiStocksResultsResponse,

    var stockDate: Date
)

fun ApiStocksResponse.toListStockEntity(): List<StockEntity> {
    return results.stocks.map { (stocksMapKey, stocksMapValue) ->
        val fullLocationList = stocksMapValue.location.split(", ")
        val countryLocation = fullLocationList.last()
        val cityLocation = fullLocationList.first()

        StockEntity(
            stocksMapKey,
            stocksMapValue.name,
            countryLocation,
            cityLocation,
            stocksMapValue.points,
            stocksMapValue.variation,
            stockDate
        )
    }
}