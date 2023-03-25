package com.omouravictor.ratesnow.data.network.hgbrasil.stock

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesnow.data.local.entity.StockEntity
import java.util.*

data class SourceRequestStockModel(
    @SerializedName("by")
    val sourceBy: String,

    @SerializedName("valid_key")
    val sourceValidKey: Boolean,

    @SerializedName("results")
    val sourceResultStocks: SourceRequestResultStockModel,

    @SerializedName("execution_time")
    val sourceExecutionTime: Double,

    @SerializedName("from_cache")
    val from_cache: Boolean
)

fun SourceRequestStockModel.toListStockEntity(): List<StockEntity> {
    val list: MutableList<StockEntity> = mutableListOf()
    val date = Date()

    sourceResultStocks.resultsStocks.forEach {
        list.add(
            StockEntity(
                it.key,
                it.value.requestStockName,
                it.value.requestStockLocation,
                it.value.requestStockPoints,
                it.value.requestStockVariation,
                date
            )
        )
    }

    return list
}