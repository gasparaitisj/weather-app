package com.justas.weather.core.domain.model

// Sorted in clockwise order
enum class CommonWindDirection(val value: String) {
    NORTH("N"),
    NORTHEAST("NE"),
    EAST("E"),
    SOUTHEAST("SE"),
    SOUTH("S"),
    SOUTHWEST("SW"),
    WEST("W"),
    NORTHWEST("NW");

    companion object {
        fun getDirection(degrees: Int?): CommonWindDirection? {
            if (degrees == null) return null
            val percentageForDirection = 360.0 / entries.size
            val offset = percentageForDirection / 2
            val index = ((degrees + offset) / percentageForDirection).toInt() % entries.size
            return entries.getOrNull(index)
        }
    }
}
