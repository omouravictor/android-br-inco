package com.omouravictor.ratesbr.util

import java.text.NumberFormat
import java.util.*

object FormatUtils {
    fun getFormattedValueForCurrencyLocale(value: Double, locale: Locale): String {
        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        return numberFormat.format(value)
    }
}