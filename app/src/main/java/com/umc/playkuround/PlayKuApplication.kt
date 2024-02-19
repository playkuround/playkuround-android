package com.umc.playkuround

import android.app.Application
import android.util.Log
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.data.RefreshTokenResponse
import com.umc.playkuround.data.User
import com.umc.playkuround.service.PreferenceUtil
import com.umc.playkuround.service.UserService
import java.text.SimpleDateFormat
import java.util.*

class PlayKuApplication : Application() {

    companion object {
        lateinit var pref : PreferenceUtil
        var user = User.getDefaultUser()
    }

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceUtil(applicationContext)
        user.load(pref)
        Log.d("userInfo", "onCreate: $user")
    }

//    fun updateAccessToken() {
//        val currentLocale = Locale("KOREAN", "KOREA")
//        val pattern = "yyyy-MM-dd HH:mm:ss"
//        val formatter = SimpleDateFormat(pattern, currentLocale)
//
//        //val expiredTime = formatter.parse(user.userTokenResponse!!.response!!.accessTokenExpiredAt)
//        val nowTime = Date()
//
//        Log.d("zdzd", "getAccessToken: $expiredTime, $nowTime")
//        var period = expiredTime.time - nowTime.time - 1000
//        if(period < 0) period = 5000
//        Log.d("zdzd", "updateAccessToken: $period")
//        //period = 5000
//
//        timer(period = period, initialDelay = period) {
//            Log.d("time set", "updateAccessToken: reissuance $period")
//            reissuanceToken()
//            this.cancel()
//        }
//    }
//
//    private fun reissuanceToken() {
//        val userService = UserService()
//        val token = user.getRefreshToken()
//        userService.setOnResponseListener(object : UserService.OnResponseListener() {
//            override fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
//                if(isSuccess) {
//                    if(body is RefreshTokenResponse) {
//                        user.userTokenResponse!!.response!!.accessToken = body.response!!.accessToken
//                        //user.userTokenResponse!!.response!!.accessTokenExpiredAt = body.response!!.accessTokenExpireTime
//                        updateAccessToken()
//                        Log.d("zdzd", "getResponseBody: $user")
//                    }
//                } else {
//                    Log.d("reissuance failed", "getResponseBody: $err")
//                }
//            }
//        }).reissuanceToken(token)
//    }

}