package com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName

data class SourceRequestResultBitcoinModel(
    @SerializedName("bitcoin")
    val resultsBitcoin: LinkedHashMap<String, SourceRequestBitcoinItemModel>
) {}
