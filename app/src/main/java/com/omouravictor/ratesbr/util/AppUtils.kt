package com.omouravictor.ratesbr.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

object BrazilianFormats {
    private val locale = Locale("pt", "BR")
    val dateFormat = SimpleDateFormat("dd/MM/yy", locale)
    val timeFormat = SimpleDateFormat("HH:mm", locale)
    val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(locale)
    val decimalFormat2Places = DecimalFormat("#0.00", DecimalFormatSymbols(locale))
    val decimalFormat3Places = DecimalFormat("#0.000", DecimalFormatSymbols(locale))
}

object Numbers {
    fun getRoundedDouble(value: Double) = round(value * 100) / 100
}