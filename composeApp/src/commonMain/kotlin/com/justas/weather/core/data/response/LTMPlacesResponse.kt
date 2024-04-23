package com.justas.weather.core.data.response
import com.justas.weather.core.domain.model.CommonCoordinates
import com.justas.weather.core.domain.model.CommonPlace
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LTMPlacesResponseItem(
    @SerialName("administrativeDivision")
    val administrativeDivision: String?,
    @SerialName("code")
    val code: String?,
    @SerialName("countryCode")
    val countryCode: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("coordinates")
    val coordinates: LTMPlacesCoordinates?
) {
    fun toModel(): CommonPlace =
        CommonPlace(
            administrativeDivision = administrativeDivision.orEmpty(),
            code = code.orEmpty(),
            countryCode = countryCode.orEmpty(),
            name = name.orEmpty(),
            coordinates =
                CommonCoordinates(
                    latitude = coordinates?.latitude,
                    longitude = coordinates?.longitude,
                ),
        )
}

@Serializable
data class LTMPlacesCoordinates(
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?,
)
