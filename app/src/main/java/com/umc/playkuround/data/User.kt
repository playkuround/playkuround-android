package com.umc.playkuround.data

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.annotations.SerializedName
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.activity.LoginActivity
import com.umc.playkuround.service.PreferenceUtil
import com.umc.playkuround.service.UserService

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

    fun getAccessToken() : String {
        return (this.userTokenResponse!!.response!!.grantType + " " + this.userTokenResponse!!.response!!.accessToken)
    }

    fun getRefreshToken() : String {
        return (this.userTokenResponse!!.response!!.grantType + " " + this.userTokenResponse!!.response!!.refreshToken)
    }

    private fun reissuanceToken() {
        val userService = UserService()
        val token = PlayKuApplication.user.getRefreshToken()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
                if(isSuccess) {
                    if(body is RefreshTokenResponse) {
                        userTokenResponse!!.response!!.accessToken = body.response!!.accessToken
                        userTokenResponse!!.response!!.accessTokenExpiredAt = body.response!!.accessTokenExpireTime
                    }
                } else {
                    Log.d("reissuance failed", "getResponseBody: $err")
                }
            }
        }).reissuanceToken(token)
    }
}