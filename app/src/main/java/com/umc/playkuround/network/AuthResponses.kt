package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class EmailResponse(
    @SerializedName(value = "isSuccess") var isSuccess: Boolean,
    @SerializedName(value = "response") var response : EmailResponseData?
)

data class EmailResponseData(
    @SerializedName(value = "expireAt") var expireAt : String,
    @SerializedName(value = "sendingCount") var sendingCount : Int
)

data class CertifyCodeResponse(
    @SerializedName(value = "isSuccess") var isSuccess: Boolean,
    @SerializedName(value = "response") var response : CertifyCodeResponseData?
)

data class CertifyCodeResponseData(
    @SerializedName(value = "grantType") var grantType : String?,
    @SerializedName(value = "accessToken") var accessToken : String?,
    @SerializedName(value = "refreshToken") var refreshToken : String?,
    @SerializedName(value = "authVerifyToken") var authVerifyToken : String?,
)

data class ReissueTokens(
    @SerializedName(value = "accessToken") var accessToken : String,
    @SerializedName(value = "refreshToken") var refreshToken : String
)