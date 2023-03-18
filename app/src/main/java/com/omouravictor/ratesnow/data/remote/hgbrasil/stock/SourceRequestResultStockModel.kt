package com.omouravictor.ratesnow.data.remote.hgbrasil.stock

import com.google.gson.annotations.SerializedName

data class SourceRequestResultStockModel(
    @SerializedName("stocks")
    val resultsStocks: LinkedHashMap<String, SourceRequestStockItemModel>
)
