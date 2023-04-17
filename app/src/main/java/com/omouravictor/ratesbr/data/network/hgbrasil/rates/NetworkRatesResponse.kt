package com.omouravictor.ratesbr.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import java.util.*

data class NetworkRatesResponse(
    @SerializedName("by")
    val sourceBy: String,

    @SerializedName("valid_key")
    val sourceValidKey: Boolean,

    @SerializedName("results")
    val sourceResultCurrency: NetworkRatesResultsResponse,

    @SerializedName("execution_time")
    val sourceExecutionTime: Double,

    @SerializedName("from_cache")
    val from_cache: Boolean,

    var rateDate: Date
)

fun NetworkRatesResponse.toListRateEntity(): List<RateEntity> {
    val list: MutableList<RateEntity> = mutableListOf()

    sourceResultCurrency.resultsCurrencies.keys.forEach { currency ->
        val rate = sourceResultCurrency.resultsCurrencies[currency]?.requestCurrencyBuy
        val variation = sourceResultCurrency.resultsCurrencies[currency]?.requestCurrencyVariation
        list.add(RateEntity(currency, rate!!, variation!!, rateDate))
    }

    return list
}