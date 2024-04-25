package com.justas.weather.app.info

import com.justas.weather.core.domain.model.CommonDailyForecastItem
import com.justas.weather.core.domain.model.CommonForecastItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

data class InfoState(
    val averageForecastItemsByDay: PersistentList<
        Triple<
            LocalDate,
            PersistentList<CommonForecastItem>,
            CommonDailyForecastItem,
        >,
    > = persistentListOf(),
    val isLoading: Boolean = true,
)
