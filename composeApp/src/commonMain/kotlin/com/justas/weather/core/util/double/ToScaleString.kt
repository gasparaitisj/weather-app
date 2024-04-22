package com.justas.weather.core.util.double

import com.ionspin.kotlin.bignum.decimal.BigDecimal

fun Double?.toScaleString(scale: Long): String =
    this?.let {
        return BigDecimal
            .fromDouble(
                double = this,
            )
            .scale(scale)
            .toPlainString()
    }.orEmpty()
