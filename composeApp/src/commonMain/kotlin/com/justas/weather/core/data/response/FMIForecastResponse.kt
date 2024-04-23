package com.justas.weather.core.data.response

import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonWindDirection
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName(
    value = "FeatureCollection",
    namespace = "http://www.opengis.net/wfs/2.0",
)
data class FMIForecastResponse(
    val member: FMIMember
) {
    fun toModel(name: String): CommonForecast {
        val items: PersistentList<CommonForecastItem> =
            buildList {
                parameters
                    .zip(timestamps)
                    .map { (parameters, instant) ->
                        CommonForecastItem(
                            airTemperature = parameters[0],
                            cloudCover = parameters[1],
                            condition = "",
                            feelsLikeTemperature = null,
                            instant = instant,
                            relativeHumidity = parameters[2],
                            seaLevelPressure = parameters[3],
                            totalPrecipitation = parameters[4],
                            windDirection =
                                CommonWindDirection.getDirection(
                                    parameters[5].roundToInt(),
                                ),
                            windGust = parameters[6],
                            windSpeed = parameters[7],
                        )
                    }
                    .forEach(::add)
            }.toPersistentList()

        return CommonForecast(
            items = items,
            provider = name,
        )
    }

    private val timestamps get() =
        member.gridSeriesObservation.result.multiPointCoverage
            .domainSet.simpleMultiPoint.positions.value
            .trimIndent()
            .split("\n")
            .map { positionValue ->
                positionValue // "[12.11,     -0.48,   12421321312]"
                    .split("\\s+".toRegex()) // ["12.11","-0.48","12421321312"]
                    .get(2) // "12421321312"
                    .toLong() // 12421321312
                    .run(Instant::fromEpochSeconds)
                    .apply {
                        val offsetDuration =
                            TimeZone
                                .currentSystemDefault()
                                .offsetAt(this)
                                .totalSeconds
                                .seconds
                        plus(offsetDuration)
                    }
            }
    private val parameters: List<List<Double>> get() =
        member.gridSeriesObservation.result.multiPointCoverage
            .rangeSet.dataBlock.reasonTupleList.value
            .trimIndent()
            .split("\n")
            .map { parameterValues ->
                parameterValues // "[12.11,     -0.48,   12421321312]"
                    .split("\\s+".toRegex())
                    .filter(String::isNotBlank) // ["12.11","-0.48","12421321312"]
                    .map(String::toDouble)
            }
}

@Serializable
@XmlSerialName(
    value = "member",
    namespace = "http://www.opengis.net/wfs/2.0",
)
data class FMIMember(
    val gridSeriesObservation: FMIGridSeriesObservation
)

@Serializable
@XmlSerialName(
    value = "GridSeriesObservation",
    namespace = "http://inspire.ec.europa.eu/schemas/omso/3.0",
)
data class FMIGridSeriesObservation(
    val result: FMIResult
)

@Serializable
@XmlSerialName(
    value = "result",
    namespace = "http://www.opengis.net/om/2.0",
)
data class FMIResult(
    val multiPointCoverage: FMIMultiPointCoverage
)

@Serializable
@XmlSerialName(
    value = "MultiPointCoverage",
    namespace = "http://www.opengis.net/gmlcov/1.0",
)
data class FMIMultiPointCoverage(
    val domainSet: FMIDomainSet,
    val rangeSet: FMIRangeSet
)

@Serializable
@XmlSerialName(
    value = "domainSet",
    namespace = "http://www.opengis.net/gml/3.2",
)
data class FMIDomainSet(
    val simpleMultiPoint: FMISimpleMultiPoint
)

@Serializable
@XmlSerialName(
    value = "SimpleMultiPoint",
    namespace = "http://www.opengis.net/gmlcov/1.0",
)
data class FMISimpleMultiPoint(
    val positions: FMIPositions
)

@Serializable
@XmlSerialName(
    value = "positions",
    namespace = "http://www.opengis.net/gmlcov/1.0",
)
data class FMIPositions(
    @XmlValue
    val value: String
)

@Serializable
@XmlSerialName(
    value = "rangeSet",
    namespace = "http://www.opengis.net/gml/3.2",
)
data class FMIRangeSet(
    val dataBlock: FMIDataBlock
)

@Serializable
@XmlSerialName(
    value = "DataBlock",
    namespace = "http://www.opengis.net/gml/3.2",
)
data class FMIDataBlock(
    val reasonTupleList: FMIDoubleOrNilReasonTupleList
)

@Serializable
@XmlSerialName(
    value = "doubleOrNilReasonTupleList",
    namespace = "http://www.opengis.net/gml/3.2",
)
data class FMIDoubleOrNilReasonTupleList(
    @XmlValue
    val value: String,
)
