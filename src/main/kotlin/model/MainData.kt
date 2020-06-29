package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainData(
    var temp: Double,
    @SerialName("temp_min")
    var minTemp: Double,
    @SerialName("temp_max")
    var maxTemp: Double,
    var pressure: Double,
    @SerialName("sea_level")
    var seaLevel: Double,
    @SerialName("grnd_level")
    var groundLevel: Double,
    var humidity: Double,
    @SerialName("temp_kf")
    var tempKf: Double
)
