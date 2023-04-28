package com.omouravictor.ratesbr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.ratesbr.presenter.bitcoins.model.BitcoinUiModel
import java.util.*

@Entity(tableName = "bitcoin_table")
data class BitcoinEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val currencyTerm: String,
    val language: String,
    val countryLanguage: String,
    val unitaryRate: Double,
    val variation: Double,
    val bitcoinDate: Date
)

fun BitcoinEntity.toBitcoinUiModel() = BitcoinUiModel(
    name,
    currencyTerm,
    language,
    countryLanguage,
    unitaryRate,
    variation,
    bitcoinDate
)
