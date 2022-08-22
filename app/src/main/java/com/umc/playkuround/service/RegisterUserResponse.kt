package com.umc.playkuround.service

import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(
    @SerializedName(value = "isSuccess") val isSuccess : Boolean,
    @SerializedName(value = "response") val response : Response,
    @SerializedName(value = "errorResponse") val errorResponse : ErrorResponse
)

data class Response(
    @SerializedName(value = "grantType") val grantType : String,
    @SerializedName(value = "accessToken") val accessToken : String,
    @SerializedName(value = "accessTokenExpiredAt") val accessTokenExpiredAt : String,
    @SerializedName(value = "refreshToken") val refreshToken : String,
    @SerializedName(value = "refreshTokenExpiredAt") val refreshTokenExpiredAt : String
)

data class ErrorResponse(
    @SerializedName(value = "status") val status : Int,
    @SerializedName(value = "code") val code : String,
    @SerializedName(value = "message") val message : String
)