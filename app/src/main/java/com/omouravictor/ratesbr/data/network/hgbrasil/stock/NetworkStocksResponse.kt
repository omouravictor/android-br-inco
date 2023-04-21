package com.omouravictor.ratesbr.data.network.hgbrasil.stock

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.StockEntity
import java.util.*

data class NetworkStocksResponse(
    @SerializedName("results")
    val results: NetworkStocksResultsResponse,

    var stockDate: Date
)

fun NetworkStocksResponse.toListStockEntity(): List<StockEntity> {
    val list: MutableList<StockEntity> = mutableListOf()

    results.stocks.forEach {
        list.add(
            StockEntity(
                it.key,
                it.value.location,
                it.value.variation,
                stockDate
            )
        )
    }

    return list
}