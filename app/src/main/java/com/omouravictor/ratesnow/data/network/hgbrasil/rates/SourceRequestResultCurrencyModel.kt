package com.omouravictor.ratesnow.data.network.hgbrasil.rates

import com.google.gson.annotations.SerializedName

data class SourceRequestResultCurrencyModel(
    @SerializedName("currencies")
    val resultsCurrencies: LinkedHashMap<String, SourceRequestCurrencyItemModel>,
)
