package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.BitCoinEntity
import java.util.*

data class NetworkBitCoinsResponse(

    @SerializedName("results")
    val results: NetworkBitCoinsResultsResponse,

    var bitcoinDate: Date
)

fun NetworkBitCoinsResponse.toListBitCoinEntity(): List<BitCoinEntity> {
    val list: MutableList<BitCoinEntity> = mutableListOf()

    results.bitcoins.forEach { bitcoinResponse ->
        list.add(
            BitCoinEntity(
                bitcoinResponse.value.name,
                bitcoinResponse.value.format[0],
                bitcoinResponse.value.format[1],
                bitcoinResponse.value.last,
                bitcoinResponse.value.variation,
                bitcoinDate
            )
        )
    }

    return list
}