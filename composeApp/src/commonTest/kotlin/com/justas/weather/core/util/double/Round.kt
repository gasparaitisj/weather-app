package com.justas.weather.core.util.double

import kotlin.test.Test
import kotlin.test.assertEquals

class RoundTest {
    @Test
    fun test_roundDownWithScale1_isCorrect() {
        val number = 10.34
        val result = number.round(1)

        assertEquals(result, 10.3)
    }

    @Test
    fun test_roundUpWithScale1_isCorrect() {
        val number = 10.35
        val actual = number.round(1)
        assertEquals(10.4, actual)
    }

    @Test
    fun test_roundWithScale4_isCorrect() {
        val number = 10.35234
        val actual = number.round(4)
        assertEquals(10.3523, actual)
    }
}
