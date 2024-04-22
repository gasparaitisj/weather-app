package com.justas.weather.core.util.double

import kotlin.math.pow
import kotlin.math.round

fun Double.round(scale: Int): Double {
    val precision = 10.0.pow(scale)
    return round(this * precision) / precision
}
