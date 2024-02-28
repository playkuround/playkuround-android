package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityLoginBinding
import com.umc.playkuround.network.AuthAPI
import com.umc.playkuround.network.ReissueTokens
import java.util.Timer
import kotlin.concurrent.timer

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private var bgGif : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLoginBtn.setOnClickListener {
            bgGif?.cancel()
            user.load(pref)
            Log.d("isoo", "checkLoginInfo: email : ${user.email}, name : ${user.nickname}, major : ${user.major}")
            if(user.major == "null") {
                Log.d("isoo", "checkLoginInfo: ${user.major}")
                val intent = Intent(this, EmailCertifyActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val authAPI = AuthAPI()
                val reissueTokens = ReissueTokens(user.userTokenResponse?.tokenData!!.accessToken, user.userTokenResponse?.tokenData!!.refreshToken)
                authAPI.setOnResponseListener(object : AuthAPI.OnResponseListener() {
                    override fun <T> getResponseBody(
                        body: T,
                        isSuccess: Boolean,
                        errorLog: String
                    ) {
                        if(isSuccess) {
                            val intent = Intent(applicationContext, MapActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                        }
                    }
                }).reissue(reissueTokens)
            }
        }

        var num = 0
        bgGif = timer(period = 300) {
            runOnUiThread {
                if(num%4 == 0) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg01)
                else if(num%4 == 1) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg02)
                else if(num%4 == 2) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg03)
                else if(num%4 == 3) binding.loginBackgroundIv.setImageResource(R.drawable.login_bg04)
                num++
            }
        }
    }

}