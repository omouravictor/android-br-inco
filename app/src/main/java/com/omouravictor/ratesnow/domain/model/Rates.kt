package com.omouravictor.ratesnow.domain.model

import com.omouravictor.ratesnow.data.network.apilayer.model.RatesItemsNetworkResponse
import com.omouravictor.ratesnow.presenter.rates.RatesDto
import java.util.*

data class Rates(
    val currencyBase: String,
    val currenciesRates: RatesItemsNetworkResponse
)

fun Rates.toListRatesDto(listToCurrencies: List<String>): List<RatesDto> {
    val list: MutableList<RatesDto> = mutableListOf()
    val date = Date()

    listToCurrencies.filterNot { toCurrency -> toCurrency == currencyBase }
        .forEach { toCurrency ->
            val rate = when (toCurrency) {
                "BRL" -> currenciesRates.bRL
                "USD" -> currenciesRates.uSD
                "EUR" -> currenciesRates.eUR
                "JPY" -> currenciesRates.jPY
                "GBP" -> currenciesRates.gBP
                "CAD" -> currenciesRates.cAD
                else -> 0.0
            }

            list.add(RatesDto(currencyBase, toCurrency, 1F, rate, date))
        }

    return list
}
