package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean?,
    @SerializedName(value = "response") var response : Response?
)

data class Response(
    @SerializedName(value = "grantType") val grantType : String,
    @SerializedName(value = "accessToken") val accessToken : String,
    @SerializedName(value = "accessTokenExpiredAt") val accessTokenExpiredAt : String,
    @SerializedName(value = "refreshToken") val refreshToken : String,
    @SerializedName(value = "refreshTokenExpiredAt") val refreshTokenExpiredAt : String
)