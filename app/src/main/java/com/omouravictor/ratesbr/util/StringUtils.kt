package com.omouravictor.ratesbr.util

import com.omouravictor.ratesbr.util.BrazilianFormats.numberFormat

object StringUtils {

    fun getVariationText(variation: Double): String {
        val formattedVariation = numberFormat.format(variation)
        return if (variation > 0.0) "+${formattedVariation}%" else "${formattedVariation}%"
    }

    fun getCurrencyNameInPortuguese(currencyTerm: String) = when (currencyTerm) {
        "BRL" -> "Real brasileiro"
        "USD" -> "Dólar americano"
        "EUR" -> "Euro"
        "GBP" -> "Libra esterlina"
        "ARS" -> "Peso argentino"
        "CAD" -> "Dólar canadense"
        "AUD" -> "Dólar australiano"
        "JPY" -> "Iene japonês"
        "CNY" -> "Yuan chinês"
        else -> currencyTerm
    }

    fun getCountryInPortuguese(country: String) = when (country) {
        "Brazil" -> "Brasil"
        "United States" -> "Estados Unidos"
        "French" -> "França"
        "Japan" -> "Japão"
        else -> country
    }

    fun getCityInPortuguese(city: String) = when (city) {
        "Sao Paulo" -> "São Paulo"
        "New York City" -> "Nova Iorque"
        "Paris" -> "Paris"
        "Tokyo" -> "Tóquio"
        else -> city
    }
}