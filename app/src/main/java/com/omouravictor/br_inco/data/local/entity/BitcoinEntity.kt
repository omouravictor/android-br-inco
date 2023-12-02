package com.omouravictor.br_inco.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omouravictor.br_inco.presenter.bitcoins.model.BitcoinUiModel
import com.omouravictor.br_inco.util.StringUtils
import java.util.Date

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
    StringUtils.getCurrencyNameInPortuguese(currencyTerm),
    currencyTerm,
    language,
    countryLanguage,
    unitaryRate,
    variation,
    bitcoinDate
)
