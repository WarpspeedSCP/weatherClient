package model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    var id: Int,
    var description: String,
    var main: String
)
