package com.justas.weather.app.home.topbar

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

class HomeTopBarViewModel(
    private val placesApi: LTMPlacesApi,
    private val forecastRepository: ForecastRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeTopBarState())
    val state = _state.asStateFlow()

    init {
        onCreated()
    }

    private fun onCreated() {
        getPlaces()
    }

    fun getPlaces() {
        viewModelScope.launch {
            _state.update { uiState ->
                uiState.copy(
                    isLoading = true,
                )
            }
            val places = placesApi.getPlaces().toPersistentList()
            _state.update { uiState ->
                uiState.copy(
                    places = places,
                    isLoading = false,
                )
            }
        }
    }

    fun onPlaceSelected(place: CommonPlace?) {
        if (place == null) return
        viewModelScope.launch {
            _state.update { uiState ->
                uiState.copy(
                    isLoading = true,
                )
            }
            val placeWithCoordinates = placesApi.getPlaceByCode(place.code)
            _state.update { uiState ->
                uiState.copy(
                    selectedPlace = placeWithCoordinates,
                    isLoading = false,
                )
            }
            forecastRepository.onRefresh(placeWithCoordinates)
        }
    }
}
