package com.omouravictor.ratesbr.data.network.hgfinanceapi.rates

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import java.util.*

data class ApiRatesResponse(
    @SerializedName("results")
    val results: ApiRatesResultsResponse,

    var rateDate: Date
)

fun ApiRatesResponse.toListRateEntity(): List<RateEntity> {
    val list: MutableList<RateEntity> = mutableListOf()

    results.currencies.keys.forEach { currencyTerm ->
        val rate = results.currencies[currencyTerm]?.buy
        val variation = results.currencies[currencyTerm]?.variation
        list.add(RateEntity(currencyTerm, rate!!, variation!!, rateDate))
    }

    return list
}