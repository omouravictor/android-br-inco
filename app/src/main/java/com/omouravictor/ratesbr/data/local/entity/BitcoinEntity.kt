package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import java.util.*

@Entity(tableName = "bitcoin_table")
data class BitcoinEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val iSO: String,
    val language: String,
    val value: Double,
    val variation: Double,
    val bitcoinDate: Date
)

fun BitcoinEntity.toBitcoinUiModel() =
    BitcoinUiModel(
        name,
        iSO,
        language,
        value,
        variation,
        bitcoinDate
    )
