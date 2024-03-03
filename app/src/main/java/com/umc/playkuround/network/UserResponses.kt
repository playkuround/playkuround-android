package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "response") var response : Any
)

data class UserTokenResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean?,
    @SerializedName(value = "response") var tokenData : TokenData?
)

data class TokenData(
    @SerializedName(value = "grantType") var grantType : String,
    @SerializedName(value = "accessToken") var accessToken : String,
    @SerializedName(value = "refreshToken") var refreshToken : String
)

data class DuplicateResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "response") var response : Boolean
)

data class UserProfileResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "response") var response : UserProfileData
)

data class UserProfileData(
    @SerializedName(value = "email") var email : String,
    @SerializedName(value = "nickname") var nickname : String,
    @SerializedName(value = "major") var major : String,
    @SerializedName(value = "highestScore") var highestScore : Int,
    @SerializedName(value = "highestRank") var highestRank : Int,
    @SerializedName(value = "attendanceDays") var attendanceDays : Int
)

data class NotificationResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val notifications: List<Notification>
)

data class Notification(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)

data class HighestScoresResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val highestScores: HighestScores
)

data class HighestScores(
    @SerializedName("highestTotalScore") val highestTotalScore: Int,
    @SerializedName("highestQuizScore") val highestQuizScore: Int,
    @SerializedName("highestTimeScore") val highestTimeScore: Int,
    @SerializedName("highestMoonScore") val highestMoonScore: Int,
    @SerializedName("highestCardScore") val highestCardScore: Int,
    @SerializedName("highestCatchScore") val highestCatchScore: Int,
    @SerializedName("highestHongBridgeScore") val highestHongBridgeScore: Int,
    @SerializedName("highestAllClearScore") val highestAllClearScore: Int,
    @SerializedName("highestMicrobeScore") val highestMicrobeScore: Int
)