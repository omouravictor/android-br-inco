package com.omouravictor.ratesnow.data.network.hgbrasil.bitcoin

import com.google.gson.annotations.SerializedName
import com.omouravictor.ratesnow.data.local.entity.BitCoinEntity
import com.omouravictor.ratesnow.presenter.bitcoins.model.BitCoinUiModel
import java.util.*

data class SourceRequestBitCoinModel(
    @SerializedName("by")
    val sourceBy: String,

    @SerializedName("valid_key")
    val sourceValidKey: Boolean,

    @SerializedName("results")
    val sourceResultBitcoin: SourceRequestResultBitcoinModel,

    @SerializedName("execution_time")
    val sourceExecutionTime: Double,

    @SerializedName("from_cache")
    val from_cache: Boolean
)

fun SourceRequestBitCoinModel.toListBitCoinEntity(): List<BitCoinEntity> {
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