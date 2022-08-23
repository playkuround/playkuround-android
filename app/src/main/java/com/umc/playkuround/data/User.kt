package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName
import com.umc.playkuround.service.PreferenceUtil

data class User(
    @SerializedName(value = "email") var email : String,
    @SerializedName(value = "nickname") var nickname : String,
    @SerializedName(value = "major") var major : String,
    var userTokenResponse : UserTokenResponse?
) {
    companion object {
        fun getDefaultUser(): User {
            val response = Response("null", "null", "null", "null", "null")
            val userTokenResponse = UserTokenResponse(true, response)
            return User("null", "null", "null", userTokenResponse)
        }
    }

    fun save(pref : PreferenceUtil) {
        pref.setString("email", this.email)
        pref.setString("nickname", this.nickname)
        pref.setString("major", this.major)
        if(this.userTokenResponse != null) {
            if(this.userTokenResponse!!.response != null) {
                pref.setString("grantType", this.userTokenResponse!!.response!!.grantType)
                pref.setString("accessToken", this.userTokenResponse!!.response!!.accessToken)
                pref.setString("accessTokenExpiredAt", this.userTokenResponse!!.response!!.accessTokenExpiredAt)
                pref.setString("refreshToken", this.userTokenResponse!!.response!!.refreshToken)
                pref.setString("refreshTokenExpiredAt", this.userTokenResponse!!.response!!.refreshTokenExpiredAt)
            }
        }
    }

    fun load(pref : PreferenceUtil) {
        this.email = pref.getString("email", "null")
        this.nickname = pref.getString("nickname", "null")
        this.major = pref.getString("major", "null")
        val grantType = pref.getString("grantType", "null")
        val accessToken = pref.getString("accessToken", "null")
        val accessTokenExpiredAt = pref.getString("accessTokenExpiredAt", "null")
        val refreshToken = pref.getString("refreshToken", "null")
        val refreshTokenExpiredAt = pref.getString("refreshTokenExpiredAt", "null")

        val response = Response(grantType, accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt)
        this.userTokenResponse = UserTokenResponse(true, response)
    }
}