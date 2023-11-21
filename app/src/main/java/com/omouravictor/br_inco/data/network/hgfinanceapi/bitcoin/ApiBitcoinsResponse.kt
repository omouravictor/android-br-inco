package com.omouravictor.br_inco.data.network.hgfinanceapi.bitcoin

import com.google.gson.annotations.SerializedName
import com.omouravictor.br_inco.data.local.entity.BitcoinEntity
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.StringUtils.getCurrencyNameInPortuguese
import java.util.*

data class ApiBitcoinsResponse(
    @SerializedName("results")
    val results: ApiBitcoinsResultsResponse,

    var bitcoinDate: Date
)

fun ApiBitcoinsResponse.toBitcoinsEntityList(): List<BitcoinEntity> {
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

fun ApiBitcoinsResponse.toBitcoinsUiModelList(): List<BitcoinUiModel> {
    return results.bitcoins.map { (_, bitcoinsMapValue) ->
        BitcoinUiModel(
            bitcoinsMapValue.name,
            getCurrencyNameInPortuguese(bitcoinsMapValue.format[0]),
            bitcoinsMapValue.format[0],
            bitcoinsMapValue.format[1].substring(0..1),
            bitcoinsMapValue.format[1].substring(3..4),
            bitcoinsMapValue.last,
            bitcoinsMapValue.variation,
            bitcoinDate
        )
    }
}