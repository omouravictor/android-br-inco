package com.omouravictor.brinco.util

import com.omouravictor.brinco.util.FormatUtils.BrazilianFormats.brNumberFormat

object StringUtils {
    fun getVariationText(variation: Double): String {
        val formattedVariation = brNumberFormat.format(variation)
        return if (variation > 0.0) "+${formattedVariation}%" else "${formattedVariation}%"
    }

    fun getCurrencyNameInPortuguese(currencyTerm: String) = when (currencyTerm) {
        "BRL" -> "Real Brasileiro"
        "USD" -> "Dólar Americano"
        "EUR" -> "Euro"
        "GBP" -> "Libra Esterlina"
        "ARS" -> "Peso Argentino"
        "CAD" -> "Dólar Canadense"
        "AUD" -> "Dólar Australiano"
        "JPY" -> "Iene Japonês"
        "CNY" -> "Yuan Chinês"
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