package com.justas.weather.app.info

import com.justas.weather.core.domain.model.CommonForecastItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

data class InfoState(
    val averageForecastItemsByHour: PersistentList<CommonForecastItem> = persistentListOf(),
    val averageForecastItemsByDay:
        PersistentList<Pair<LocalDate, PersistentList<CommonForecastItem>>> = persistentListOf(),
    val isLoading: Boolean = true,
)
