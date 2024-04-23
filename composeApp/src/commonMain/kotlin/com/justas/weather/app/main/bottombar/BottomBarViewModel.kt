package com.justas.weather.app.main.bottombar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BottomBarViewModel : ViewModel() {
    private val _state = MutableStateFlow(BottomBarState())
    val state = _state.asStateFlow()

    fun onItemSelected(item: BottomBarItem) {
        _state.update { uiState ->
            uiState.copy(
                selectedItem = item
            )
        }
    }
}
