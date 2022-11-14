package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.PlayKuApplication.Companion.pref
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.databinding.ActivityPolicyAgreeBinding
import com.umc.playkuround.databinding.ActivitySplashBinding
import com.umc.playkuround.fragment.BadgeFragment
import com.umc.playkuround.fragment.HomeFragment

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLoginInfo()
        finish()
    }

    private fun checkLoginInfo() {
        user.load(pref)
        Log.d("user info", "checkLoginInfo: email : ${user.email}, name : ${user.nickname}, major : ${user.major}")
        if(user.major == "null") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}