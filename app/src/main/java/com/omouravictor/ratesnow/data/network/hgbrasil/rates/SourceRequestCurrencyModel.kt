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
    val list: MutableList<RateEntity> = mutableListOf()
    val date = Date()

    currencies.split(",").forEach { currency ->
        val rate = when (currency) {
            "USD" -> sourceResultCurrency.resultsCurrencies["USD"]?.requestCurrencyBuy ?: 0.0
            "EUR" -> sourceResultCurrency.resultsCurrencies["EUR"]?.requestCurrencyBuy ?: 0.0
            "JPY" -> sourceResultCurrency.resultsCurrencies["JPY"]?.requestCurrencyBuy ?: 0.0
            "GBP" -> sourceResultCurrency.resultsCurrencies["GBP"]?.requestCurrencyBuy ?: 0.0
            "CAD" -> sourceResultCurrency.resultsCurrencies["CAD"]?.requestCurrencyBuy ?: 0.0
            "AUD" -> sourceResultCurrency.resultsCurrencies["AUD"]?.requestCurrencyBuy ?: 0.0
            else -> -1.0
        }

        list.add(RateEntity(currency, rate, date))
    }

    return list
}