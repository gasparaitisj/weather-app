package com.justas.weather.app.main.topbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justas.weather.core.data.network.ltm.LTMPlacesApi
import com.justas.weather.core.domain.model.CommonPlace
import com.justas.weather.core.domain.repository.ForecastRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopBarViewModel(
    private val placesApi: LTMPlacesApi,
    private val forecastRepository: ForecastRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(TopBarState())
    val state = _state.asStateFlow()

    init {
        onCreated()
    }

    private fun onCreated() {
        viewModelScope.launch {
            _state.update { uiState ->
                val places = placesApi.getPlaces().toPersistentList()
                uiState.copy(
                    places = places,
                )
            }
        }
    }

    fun onPlaceSelected(place: CommonPlace?) {
        if (place == null) return
        viewModelScope.launch {
            _state.update { uiState ->
                uiState.copy(
                    selectedPlace = CommonPlace(name = place.name),
                )
            }
            val placeWithCoordinates = placesApi.getPlaceByCode(place.code)
            _state.update { uiState ->
                uiState.copy(
                    selectedPlace = place,
                )
            }
            forecastRepository.onRefresh(placeWithCoordinates)
        }
    }
}
