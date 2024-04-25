package com.justas.weather.app.info

import com.justas.weather.core.domain.model.CommonForecastItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class InfoState(
    val averageForecastItems: PersistentList<CommonForecastItem> = persistentListOf()
)
