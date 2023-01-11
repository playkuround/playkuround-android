package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.PlayKuApplication.Companion.pref
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.data.RefreshTokenResponse
import com.umc.playkuround.data.UserTokenResponse
import com.umc.playkuround.databinding.ActivityPolicyAgreeBinding
import com.umc.playkuround.databinding.ActivitySplashBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.fragment.BadgeFragment
import com.umc.playkuround.fragment.HomeFragment
import com.umc.playkuround.service.UserService

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding
    lateinit private var loading : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = LoadingDialog(this)
        checkLoginInfo()
        finish()
    }

    private fun checkLoginInfo() {
        user.load(pref)
        Log.d("user info", "checkLoginInfo: email : ${user.email}, name : ${user.nickname}, major : ${user.major}")
        if(user.major == "null") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            loading.show()
            login()
        }
    }

    private fun login() {
        val userService = UserService()
        val token = user.getAccessToken()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is UserTokenResponse) {
                        user.userTokenResponse = body.copy()
                        user.save(pref)

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    if(err == "A004") { // 유효하지 않은 토큰
                        reissuanceToken()
                    } else if(err == "서버 연결에 실패하였습니다. 네트워크를 확인해주세요.") {
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.d("login failed", "getResponseBody: $err")
                        Toast.makeText(applicationContext, "로그인에 실패하였습니다!\n다시 로그인하여 주세요.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }).login(token)
    }

    private fun reissuanceToken() {
        val userService = UserService()
        val token = user.getRefreshToken()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
                if(isSuccess) {
                    if(body is RefreshTokenResponse) {
                        user.userTokenResponse!!.response!!.accessToken = body.response!!.accessToken
                        user.userTokenResponse!!.response!!.accessTokenExpiredAt = body.response!!.accessTokenExpireTime
                        login()
                    }
                } else {
                    Log.d("reissuance failed", "getResponseBody: $err")
                    Toast.makeText(applicationContext, "로그인에 실패하였습니다!\n다시 로그인하여 주세요.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }).reissuanceToken(token)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(loading != null && loading.isShowing) loading.dismiss()
    }

}