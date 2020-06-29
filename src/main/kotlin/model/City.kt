package model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    var id: Int,
    var name: String,
    var coord: LatLng,
    var country: String,
    var population: Int
)
