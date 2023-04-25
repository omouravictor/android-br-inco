package com.omouravictor.ratesbr.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

object BrazilianFormats {
    private val locale = Locale("pt", "BR")
    val timeFormat = SimpleDateFormat("HH:mm", locale)
    val dateFormat = SimpleDateFormat("dd/MM/yy", locale)
    val numberFormat: NumberFormat = NumberFormat.getNumberInstance(locale)
    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(locale)
}

object Numbers {
    fun getRoundedDouble(value: Double) = round(value * 100) / 100
}