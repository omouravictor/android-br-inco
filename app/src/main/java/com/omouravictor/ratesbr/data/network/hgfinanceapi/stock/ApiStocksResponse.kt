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
    val list: MutableList<StockEntity> = mutableListOf()

    results.stocks.forEach {
        list.add(
            StockEntity(
                it.key,
                it.value.name,
                it.value.location,
                it.value.points,
                it.value.variation,
                stockDate
            )
        )
    }

    return list
}