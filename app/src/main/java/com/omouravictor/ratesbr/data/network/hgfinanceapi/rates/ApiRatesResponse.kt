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
    return results.currencies.mapNotNull { (currenciesMapKey, currenciesMapValue) ->
        RateEntity(
            currenciesMapKey,
            currenciesMapValue.buy,
            currenciesMapValue.variation,
            rateDate
        )
    }
}