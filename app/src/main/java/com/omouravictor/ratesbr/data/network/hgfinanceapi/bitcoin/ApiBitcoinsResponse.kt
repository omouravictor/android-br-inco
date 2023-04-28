package com.omouravictor.ratesbr.data.network.hgfinanceapi.bitcoin

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.BitcoinEntity
import java.util.*

data class ApiBitcoinsResponse(
    @SerializedName("results")
    val results: ApiBitcoinsResultsResponse,

    var bitcoinDate: Date
)

fun ApiBitcoinsResponse.toListBitcoinEntity(): List<BitcoinEntity> {
    return results.bitcoins.map { (_, bitcoinsMapValue) ->
        BitcoinEntity(
            bitcoinsMapValue.name,
            bitcoinsMapValue.format[0],
            bitcoinsMapValue.format[1].substring(0..1),
            bitcoinsMapValue.format[1].substring(3..4),
            bitcoinsMapValue.last,
            bitcoinsMapValue.variation,
            bitcoinDate
        )
    }
}