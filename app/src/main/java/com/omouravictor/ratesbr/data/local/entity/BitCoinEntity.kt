package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitCoinUiModel
import java.util.*

@Entity(tableName = "bitcoin_table")
data class BitCoinEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val iSO: String,
    val language: String,
    val value: Double,
    val variation: Double,
    val date: Date
)

fun BitCoinEntity.toBitCoinUiModel() =
    BitCoinUiModel(
        name,
        iSO,
        language,
        value,
        variation,
        date
    )
