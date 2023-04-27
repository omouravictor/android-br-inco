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