package com.omouravictor.ratesnow.data.network.apilayer.model

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesnow.domain.model.Rates
import java.util.Date

data class RatesNetworkResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: RatesItemsNetworkResponse
)

fun RatesNetworkResponse.toRates() = Rates(date = Date(), currencyBase = base, currenciesRates = rates)