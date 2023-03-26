package com.omouravictor.ratesbr.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesbr.data.local.entity.BitCoinEntity
import java.util.*

data class NetworkBitCoinResult(
    @SerializedName("by")
    val sourceBy: String,

    @SerializedName("valid_key")
    val sourceValidKey: Boolean,

    @SerializedName("results")
    val sourceResultBitcoin: NetworkBitCoinsResultsResponse,

    @SerializedName("execution_time")
    val sourceExecutionTime: Double,

    @SerializedName("from_cache")
    val from_cache: Boolean
)

fun NetworkBitCoinResult.toListBitCoinEntity(): List<BitCoinEntity> {
    val list: MutableList<BitCoinEntity> = mutableListOf()
    val date = Date()

    sourceResultBitcoin.resultsBitcoin.forEach {
        list.add(
            BitCoinEntity(
                it.key,
                it.value.requestBitcoinBrokerName,
                it.value.requestBitcoinFormat[0],
                it.value.requestBitcoinFormat[1],
                it.value.requestBitcoinBrokerLast,
                it.value.requestBitcoinBrokerBuy,
                it.value.requestBitcoinBrokerSell,
                it.value.requestBitcoinBrokerVariation,
                date
            )
        )
    }

    return list
}