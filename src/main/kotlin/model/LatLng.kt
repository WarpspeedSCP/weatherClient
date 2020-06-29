package model

import kotlinx.serialization.Serializable

@Serializable
data class LatLng(
    var lat: Double,
    var lon: Double
)
