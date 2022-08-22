package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.data.User
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
            val user = User("test12@test.com", "test12", "컴퓨터공학부")
            userService.register(user)
        }
    }

}