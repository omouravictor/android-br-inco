package com.omouravictor.ratesbr.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object BrazilianFormatUtils {
    private val locale = Locale("pt", "BR")
    val timeFormat = SimpleDateFormat("HH:mm", locale)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", locale)
    val numberFormat: NumberFormat = NumberFormat.getNumberInstance(locale)
    val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(locale)
}