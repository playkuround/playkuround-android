package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName(value = "latitude") var latitude : Double,
    @SerializedName(value = "longitude") var longitude : Double
)
