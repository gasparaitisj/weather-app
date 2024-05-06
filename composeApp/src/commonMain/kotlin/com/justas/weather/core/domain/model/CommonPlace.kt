package com.justas.weather.core.domain.model

data class CommonPlace(
    val administrativeDivision: String = "",
    val code: String = "",
    val countryName: String = "",
    val countryCode: String = "",
    val name: String = "",
    val coordinates: CommonCoordinates = CommonCoordinates()
) {
    companion object {
        val VILNIUS =
            CommonPlace(
                administrativeDivision = "Vilniaus miesto savivaldybė",
                code = "vilnius",
                countryCode = "LT",
                name = "Vilnius",
                coordinates =
                    CommonCoordinates(
                        latitude = 54.687046,
                        longitude = 25.282911,
                    ),
            )
    }
}
