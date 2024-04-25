package com.justas.weather.core.util.instant

import kotlin.time.Duration
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun Instant.isBefore(duration: Duration) = plus(duration) < Clock.System.now()

fun Instant.isAfter(duration: Duration) = plus(duration) > Clock.System.now()
