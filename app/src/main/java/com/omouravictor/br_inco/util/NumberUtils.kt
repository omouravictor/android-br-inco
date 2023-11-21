package com.omouravictor.br_inco.util

import kotlin.math.round

object NumberUtils {
    fun getRoundedDouble(value: Double) = round(value * 100) / 100
}