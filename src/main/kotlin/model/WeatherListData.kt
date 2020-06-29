package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherListData(
    var dt: Long,
    @SerialName("dt_txt")
    var dtStr: String,
    var main: MainData,
    var wind: Wind,
    var weather: List<Weather>
)
