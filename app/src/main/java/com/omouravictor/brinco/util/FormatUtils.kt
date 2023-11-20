package com.omouravictor.brinco.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

object FormatUtils {
    object BrazilianFormats {
        private val brLocale = Locale("pt", "BR")
        val brTimeFormat = SimpleDateFormat("HH:mm", brLocale)
        val brDateFormat = SimpleDateFormat("dd/MM/yy", brLocale)
        val brNumberFormat: NumberFormat = NumberFormat.getNumberInstance(brLocale)
        val brCurrencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(brLocale)
    }

    fun getFormattedValueForCurrencyLocale(value: Double, locale: Locale): String =
        NumberFormat.getCurrencyInstance(locale).format(value)
}