package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName

data class UserTokenResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean?,
    @SerializedName(value = "response") var response : Response?
)

data class Response(
    @SerializedName(value = "grantType") var grantType : String,
    @SerializedName(value = "accessToken") var accessToken : String,
    @SerializedName(value = "accessTokenExpiredAt") var accessTokenExpiredAt : String,
    @SerializedName(value = "refreshToken") var refreshToken : String,
    @SerializedName(value = "refreshTokenExpiredAt") var refreshTokenExpiredAt : String
)

data class DuplicateResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "response") var response : Boolean
)

data class LogoutResponse(
    @SerializedName(value = "isSuccess") var isSuccess: Boolean?
)

data class RefreshTokenResponse(
    @SerializedName(value = "isSuccess") var isSuccess: Boolean?,
    @SerializedName(value = "response") var response: RefreshResponse
)

data class RefreshResponse(
    @SerializedName(value = "grantType") var grantType : String,
    @SerializedName(value = "accessToken") var accessToken : String,
    @SerializedName(value = "accessTokenExpireTime") var accessTokenExpireTime : String
)

data class EmailResponse(
    @SerializedName(value = "isSuccess") var isSuccess: Boolean,
    @SerializedName(value = "response") var response : EmailResponseData?
)

data class EmailResponseData(
    @SerializedName(value = "expireAt") var expireAt : String,
    @SerializedName(value = "sendingCount") var sendingCount : Int
)

data class EmailCertifyResponse(
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "response") var response : Boolean
)