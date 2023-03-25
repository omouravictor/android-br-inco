package com.omouravictor.ratesnow.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesnow.data.local.entity.RateEntity
import java.util.*

data class SourceRequestCurrencyModel(
    @SerializedName("by")
    val sourceBy: String,

    @SerializedName("valid_key")
    val sourceValidKey: Boolean,

    @SerializedName("results")
    val sourceResultCurrency: SourceRequestResultCurrencyModel,

    @SerializedName("execution_time")
    val sourceExecutionTime: Double,

    @SerializedName("from_cache")
    val from_cache: Boolean
)

fun SourceRequestCurrencyModel.toListRateEntity(currencies: String): List<RateEntity> {
    val listCurrencies = currencies.split(",")
    val list: MutableList<RateEntity> = mutableListOf()
    val rateDate = Date()

    listCurrencies.forEach { currency ->
        val rate = sourceResultCurrency.resultsCurrencies[currency]?.requestCurrencyBuy ?: -1.0
        list.add(RateEntity(currency, "BRL", rate, rateDate))
    }

    return list
}