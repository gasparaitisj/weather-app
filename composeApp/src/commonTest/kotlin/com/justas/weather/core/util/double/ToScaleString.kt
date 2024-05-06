package com.justas.weather.core.util.double

import kotlin.test.Test
import kotlin.test.assertEquals

class ToScaleStringTest {
    @Test
    fun test_toScaleStringScale2_isCorrect() {
        val number = 10.34234
        val result = number.toScaleString(2)

        assertEquals(result, "10.34")
    }

    @Test
    fun test_toScaleStringScale4_isCorrect() {
        val number = 10.342456
        val result = number.toScaleString(4)

        assertEquals(result, "10.3425")
    }
}
