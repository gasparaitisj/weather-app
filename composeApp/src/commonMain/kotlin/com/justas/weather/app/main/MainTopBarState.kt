package com.justas.weather.app.main

import com.justas.weather.core.domain.model.CommonPlace
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class MainTopBarState(
    val places: PersistentList<CommonPlace> = persistentListOf(),
    val selectedPlace: CommonPlace = CommonPlace(),
    val isDropdownMenuExpanded: Boolean = false,
    val dropdownMenuTextFieldValue: String = "",
)
