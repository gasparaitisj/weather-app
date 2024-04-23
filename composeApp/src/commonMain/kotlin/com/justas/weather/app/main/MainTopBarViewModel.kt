package com.justas.weather.app.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justas.weather.core.data.network.ltm.LTMPlacesApi
import com.justas.weather.core.domain.model.CommonPlace
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainTopBarViewModel(
    private val placesApi: LTMPlacesApi,
) : ViewModel() {
    private val _state = MutableStateFlow(MainTopBarState())
    val state = _state.asStateFlow()

    init {
        onCreated()
    }

    private fun onCreated() {
        viewModelScope.launch {
            _state.update { uiState ->
                val places = placesApi.getPlaces().toPersistentList()
                val place = places.firstOrNull() ?: CommonPlace()
                uiState.copy(
                    places = places,
                    selectedPlace = place,
                )
            }
        }
    }

    fun onPlaceSelected(place: CommonPlace) {
        _state.update { uiState ->
            uiState.copy(
                selectedPlace = place,
            )
        }
    }

    fun onDropdownMenuExpandedChange(isExpanded: Boolean) {
        _state.update { uiState ->
            uiState.copy(
                isDropdownMenuExpanded = isExpanded,
            )
        }
    }

    fun onDropdownMenuTextFieldValueChange(value: String) {
        _state.update { uiState ->
            uiState.copy(
                dropdownMenuTextFieldValue = value,
            )
        }
    }
}
