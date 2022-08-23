package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        binding.loginLogoIv.setOnClickListener {
            val userService = UserService()
            val user = User("test13@test.com", "test13", "컴퓨터공학부", null)
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body : T, isSuccess : Boolean, err : String) {
                    if(isSuccess) {
                        if(body is UserTokenResponse)
                            user.userTokenResponse = body
                        Log.d("retrofit", "getResponseBody: " + user.userTokenResponse?.response?.accessToken)
                    } else {
                        Log.d("retrofit", "getResponseBody: $err")
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            })
            userService.register(user)
        }
    }

}