package com.omouravictor.ratesbr.util

import kotlin.math.round

object Numbers {
    fun getRoundedDouble(value: Double) = round(value * 100) / 100
}