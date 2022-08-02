package com.omouravictor.ratesnow.api.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName

data class SourceRequestResultBitcoinModel(
    @SerializedName("bitcoin")
    val resultsBitcoin: LinkedHashMap<String, SourceRequestBitcoinItemModel>
) {}
