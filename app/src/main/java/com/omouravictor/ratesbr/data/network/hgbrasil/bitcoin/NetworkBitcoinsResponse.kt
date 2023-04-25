package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.BitcoinEntity
import java.util.*

data class NetworkBitcoinsResponse(

    @SerializedName("results")
    val results: NetworkBitcoinsResultsResponse,

    var bitcoinDate: Date
)

fun NetworkBitcoinsResponse.toListBitcoinEntity(): List<BitcoinEntity> {
    val list: MutableList<BitcoinEntity> = mutableListOf()

    results.bitcoins.forEach { bitcoinResponse ->
        list.add(
            BitcoinEntity(
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