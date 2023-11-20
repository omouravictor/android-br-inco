package com.omouravictor.brinco.util

import kotlin.math.round

object NumberUtils {
    fun getRoundedDouble(value: Double) = round(value * 100) / 100
}