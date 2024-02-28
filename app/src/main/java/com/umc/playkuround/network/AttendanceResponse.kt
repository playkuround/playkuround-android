package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val response: AttendanceResponseData
)

data class AttendanceResponseData(
    @SerializedName("attendances") val attendances: List<String>
)

data class UserLocation(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)