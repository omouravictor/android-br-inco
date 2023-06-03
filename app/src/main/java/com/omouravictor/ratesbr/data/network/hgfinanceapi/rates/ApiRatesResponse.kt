package com.omouravictor.ratesbr.data.network.hgfinanceapi.rates

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.RateEntity
import com.omouravictor.ratesbr.presenter.rates.model.RateUiModel
import com.omouravictor.ratesbr.util.StringUtils.getCurrencyNameInPortuguese
import java.util.*

data class ApiRatesResponse(
    @SerializedName("results")
    val results: ApiRatesResultsResponse,

    var rateDate: Date
)

fun ApiRatesResponse.toRatesEntityList(): List<RateEntity> {
    return results.currencies.map { (currenciesMapKey, currenciesMapValue) ->
        RateEntity(
            currenciesMapKey,
            currenciesMapValue.buy,
            currenciesMapValue.variation,
            rateDate
        )
    }
}

fun ApiRatesResponse.toRatesUiModelList(): List<RateUiModel> {
    return results.currencies.map { (currenciesMapKey, currenciesMapValue) ->
        RateUiModel(
            getCurrencyNameInPortuguese(currenciesMapKey),
            currenciesMapKey,
            currenciesMapValue.buy,
            currenciesMapValue.variation,
            rateDate
        )
    }
}