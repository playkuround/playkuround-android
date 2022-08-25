package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.PlayKuApplication.Companion.pref
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.data.DuplicateResponse
import com.umc.playkuround.data.RefreshTokenResponse
import com.umc.playkuround.data.User
import com.umc.playkuround.data.UserTokenResponse
import com.umc.playkuround.databinding.ActivityLoginBinding
import com.umc.playkuround.service.UserService

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLoginBtn.setOnClickListener {
            val intent = Intent(this, EmailCertifyActivity::class.java)
            startActivity(intent)
        }

        testing()
    }

    private fun testing() {
        /*binding.loginLogoIv.setOnClickListener {
            val userService = UserService()
            val temp = User("test21@test.com", "test21", "컴퓨터공학부", null)
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
                    if(isSuccess) {
                        if(body is UserTokenResponse)
                            temp.userTokenResponse = body
                        user = temp.copy()
                        user.save(pref)
                        Log.d("userInfo", "onCreate: $user")
                    } else {
                        Log.d("retrofit", "getResponseBody: $err")
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).register(temp)
        }*/

        /*binding.loginLogoIv.setOnClickListener {
            val userService = UserService()
            val nickname = "test17"
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if(isSuccess) {
                        if(body is DuplicateResponse)
                            Log.d("retrofit", "getResponseBody: " + body.response)
                    } else {
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).isDuplicate(nickname)
        }*/

        /*binding.loginLogoIv.setOnClickListener {
            val userService = UserService()
            val token = user.getAccessToken()
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if(isSuccess) {
                        if(body is UserTokenResponse)
                            user.userTokenResponse = body
                        user.save(pref)
                        Log.d("userInfo", "onCreate: $user")
                    } else {
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).login(token)
        }*/

        /*binding.loginLogoIv.setOnClickListener {
            val userService = UserService()
            val token = user.getAccessToken()
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if(isSuccess) {
                        Log.d("retrofit", "getResponseBody(logout): logout success!!")
                    } else {
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).logout(token)
        }*/

        binding.loginLogoIv.setOnClickListener {
            val userService = UserService()
            val token = user.getRefreshToken()
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
                    if(isSuccess) {
                        if(body is RefreshTokenResponse) {
                            user.userTokenResponse!!.response!!.accessToken = body.response!!.accessToken
                            user.userTokenResponse!!.response!!.accessTokenExpiredAt = body.response!!.accessTokenExpireTime
                        }
                        user.save(pref)
                        Log.d("userInfo", "onCreate(reissuance): $user")
                    } else {
                        Log.d("retrofit", "getResponseBody: $err")
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).reissuanceToken(token)
        }
    }

}