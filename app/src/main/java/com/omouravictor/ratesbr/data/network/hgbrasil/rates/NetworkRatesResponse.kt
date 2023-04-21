package com.omouravictor.ratesbr.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import java.util.*

data class NetworkRatesResponse(
    @SerializedName("results")
    val results: NetworkRatesResultsResponse,

    var rateDate: Date
)

fun NetworkRatesResponse.toListRateEntity(): List<RateEntity> {
    val list: MutableList<RateEntity> = mutableListOf()

    results.currencies.keys.forEach { currencyTerm ->
        val rate = results.currencies[currencyTerm]?.buy
        val variation = results.currencies[currencyTerm]?.variation
        list.add(RateEntity(currencyTerm, rate!!, variation!!, rateDate))
    }

    return list
}