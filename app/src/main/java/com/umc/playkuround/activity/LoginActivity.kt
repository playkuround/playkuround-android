package com.umc.playkuround.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.PlayKuApplication.Companion.pref
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.RefreshTokenResponse
import com.umc.playkuround.data.UserTokenResponse
import com.umc.playkuround.databinding.ActivityLoginBinding
import com.umc.playkuround.dialog.SlideUpDialog
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

//        binding.loginLogoIv.setOnClickListener {
//            //onSlideUpDialog()
//            val intent = Intent(this, MiniGameQuizActivity::class.java)
//            startActivity(intent)
//        }

        //testing()
    }

    private fun onSlideUpDialog() {
        var contentView: View = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.dialog_badge_info, null)
        val slideupPopup = SlideUpDialog.Builder(this)
            .setContentView(contentView)
            .create()
        slideupPopup.show()
        slideupPopup.backgroundView?.setOnClickListener {
            slideupPopup.dismissAnim()
        }
    }

    private fun testing() {
        binding.loginLogoIv.setOnClickListener {
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
        }
    }

}