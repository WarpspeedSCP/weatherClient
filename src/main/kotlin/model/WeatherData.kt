package model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    var cod: String,
    var message: Double,
    var cnt: Int,
    var list: List<WeatherListData>,
    var city: City
)

