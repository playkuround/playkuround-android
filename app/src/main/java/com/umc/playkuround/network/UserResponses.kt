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
    @SerializedName(value = "attendanceDays") var attendanceDays : Int
)