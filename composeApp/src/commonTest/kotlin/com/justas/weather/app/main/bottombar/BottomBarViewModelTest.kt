package com.justas.weather.app.main.bottombar

import app.cash.turbine.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest

class BottomBarViewModelTest {
    @Test
    fun onItemSelected_itemIsSelectedCorrectly(): TestResult =
        runTest {
            val viewModel = BottomBarViewModel()
            val expected = BottomBarState(selectedItem = BottomBarItem.INFO)

            viewModel.onItemSelected(BottomBarItem.INFO)

            viewModel.state.test {
                assertEquals(expected, awaitItem())
            }
        }
}
