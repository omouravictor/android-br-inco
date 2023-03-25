package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitCoinUiModel
import java.util.*

@Entity(tableName = "bitcoin_table")
data class BitCoinEntity(
    @PrimaryKey(autoGenerate = false)
    val bitcoinTerm: String,
    val bitcoinName: String,
    val bitcoinISO: String,
    val bitcoinLanguage: String,
    val bitcoinLast: Double,
    val bitcoinBuy: Double,
    val bitcoinSell: Double,
    val bitcoinVariation: Double,
    val date: Date
)

fun BitCoinEntity.toBitCoinUiModel() =
    BitCoinUiModel(
        bitcoinName,
        bitcoinISO,
        bitcoinLanguage,
        bitcoinLast,
        bitcoinVariation,
        date
    )
