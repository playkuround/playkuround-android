package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class LandmarkResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val response: LandmarkInfo
)

data class LandmarkInfo(
    @SerializedName("name") val name: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("landmarkId") val landmarkId: Int
)

data class LandmarkTopResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val response: RankItem
)

data class AdventureData(
    @SerializedName("landmarkId") val landmarkId: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("score") val score: Int,
    @SerializedName("scoreType") val scoreType: String
)