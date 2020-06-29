package model

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    var speed: Double,
    var deg: Double
)
