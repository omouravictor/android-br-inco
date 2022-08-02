package com.omouravictor.ratesnow.api.hgbrasil.stock

import com.google.gson.annotations.SerializedName

data class SourceRequestResultStockModel(
    @SerializedName("stocks")
    val resultsStocks: LinkedHashMap<String, SourceRequestStockItemModel>
)
