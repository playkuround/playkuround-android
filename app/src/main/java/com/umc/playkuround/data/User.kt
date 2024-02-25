package com.umc.playkuround.data

import com.google.gson.annotations.SerializedName
import com.umc.playkuround.network.TokenData
import com.umc.playkuround.network.UserTokenResponse
import com.umc.playkuround.util.PreferenceUtil

data class User(
    @SerializedName(value = "email") var email : String,
    @SerializedName(value = "nickname") var nickname : String,
    @SerializedName(value = "major") var major : String,
    @SerializedName(value = "authVerifyToken") var verifyToken : String,
    var highestScore : Int,
    var userTokenResponse : UserTokenResponse?
) {
    companion object {
        fun getDefaultUser(): User {
            val tokenData = TokenData("null", "null", "null")
            val userTokenResponse = UserTokenResponse(true, tokenData)
            return User("null", "null", "null", "null", 0, userTokenResponse)
        }
    }

    fun save(pref : PreferenceUtil) {
        pref.setString("email", this.email)
        pref.setString("nickname", this.nickname)
        pref.setString("major", this.major)
        if(this.userTokenResponse != null) {
            if(this.userTokenResponse!!.tokenData != null) {
                pref.setString("grantType", this.userTokenResponse!!.tokenData!!.grantType)
                pref.setString("accessToken", this.userTokenResponse!!.tokenData!!.accessToken)
                pref.setString("refreshToken", this.userTokenResponse!!.tokenData!!.refreshToken)
            }
        }
    }

    fun load(pref : PreferenceUtil) {
        this.email = pref.getString("email", "null")
        this.nickname = pref.getString("nickname", "null")
        this.major = pref.getString("major", "null")
        val grantType = pref.getString("grantType", "null")
        val accessToken = pref.getString("accessToken", "null")
        val refreshToken = pref.getString("refreshToken", "null")

        val tokenData = TokenData(grantType, accessToken, refreshToken)
        this.userTokenResponse = UserTokenResponse(true, tokenData)
    }

    fun getAccessToken() : String {
        return (this.userTokenResponse!!.tokenData!!.grantType + " " + this.userTokenResponse!!.tokenData!!.accessToken)
    }

    fun getRefreshToken() : String {
        return (this.userTokenResponse!!.tokenData!!.grantType + " " + this.userTokenResponse!!.tokenData!!.refreshToken)
    }

}