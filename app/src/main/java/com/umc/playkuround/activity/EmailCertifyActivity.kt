package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.network.EmailResponse
import com.umc.playkuround.network.TokenData
import com.umc.playkuround.network.UserTokenResponse
import com.umc.playkuround.databinding.ActivityEmailCertifyBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.SlideUpDialog
import com.umc.playkuround.network.AuthAPI
import com.umc.playkuround.network.CertifyCodeResponse
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.SoundPlayer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class EmailCertifyActivity : AppCompatActivity() {

    lateinit var binding : ActivityEmailCertifyBinding

    private var email : String = ""
    private var certifyCode : String = ""
    private var certifyTimer : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailCertifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        // 이메일 입력 들어옴
        binding.emailGetEmailEt.doAfterTextChanged{
            binding.emailRequestCodeBtn.isEnabled = !it.isNullOrBlank()
        }

        // 인증요청 버튼 클릭
        binding.emailRequestCodeBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            email = binding.emailGetEmailEt.text.toString() + "@konkuk.ac.kr"
            requestCode(email)

            binding.emailRequestCountTv.visibility = View.INVISIBLE
            binding.emailGotoKonkukEmailTv.visibility = View.INVISIBLE
            binding.emailInputCodeCl.visibility = View.INVISIBLE
            binding.emailCertifyBtn.visibility = View.INVISIBLE
            binding.emailInputCodeEt.text.clear()

            binding.emailRequestCodeBtn.isEnabled = false

            if(certifyTimer != null)
                certifyTimer?.cancel()
        }

        // 건국대학교 이메일 이동 클릭
        binding.emailGotoKonkukEmailTv.setOnClickListener {
            gotoKonkukEmail()
        }

        // 인증코드 입력 들어옴
        binding.emailInputCodeEt.doAfterTextChanged {
            binding.emailCertifyBtn.isEnabled = !it.isNullOrBlank()
            binding.emailWarnNotEqualTv.visibility = View.GONE
            binding.emailInputCodeCl.background = ContextCompat.getDrawable(this, R.drawable.edit_text)
            binding.emailInputCodeEt.setTextColor(Color.BLACK)
        }

        // 인증하기 버튼 클릭
        binding.emailCertifyBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            certifyCode = binding.emailInputCodeEt.text.toString()
            isCodeCorrect(email, certifyCode)
        }
    }

    private fun gotoKonkukEmail() {
        val intentURL = Intent(Intent.ACTION_VIEW, Uri.parse("http://kumail.konkuk.ac.kr/"))
        startActivity(intentURL)
    }

    private fun requestCode(email : String) {
        // send email to server
        val loading = LoadingDialog(this)
        loading.show()
        val authAPI = AuthAPI()
        authAPI.setOnResponseListener(object : AuthAPI.OnResponseListener() {
            @SuppressLint("SetTextI18n")
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loading.dismiss()
                binding.emailRequestCodeBtn.text = resources.getText(R.string.request_code_again)
                binding.emailRequestCodeBtn.isEnabled = true

                if(isSuccess) {
                    if(body is EmailResponse) {
                        binding.emailGotoKonkukEmailTv.visibility = View.VISIBLE
                        binding.emailInputCodeCl.visibility = View.VISIBLE
                        binding.emailCertifyBtn.visibility = View.VISIBLE

                        binding.emailRequestCountTv.visibility = View.VISIBLE
                        binding.emailRequestCountTv.text = "오늘 인증 요청 횟수가 " + (5 - body.response!!.sendingCount) + "회 남았습니다."

                        startTimer(body.response!!.expireAt)
                    } else {
                        Toast.makeText(applicationContext, "서버의 응답이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()

                    binding.emailGotoKonkukEmailTv.visibility = View.INVISIBLE
                    binding.emailInputCodeCl.visibility = View.INVISIBLE
                    binding.emailCertifyBtn.visibility = View.INVISIBLE

                    binding.emailRequestCountTv.visibility = View.INVISIBLE
                }
            }
        }).sendCode(email)
    }

    private fun isCodeCorrect(email : String, code : String) : Boolean {
        val loading = LoadingDialog(this)
        loading.show()
        val authAPI = AuthAPI()
        authAPI.setOnResponseListener(object : AuthAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                loading.dismiss()
                if(isSuccess) {
                    if(body is CertifyCodeResponse) {
                        if(body.response!!.grantType != null) {
                            val userTokenData = UserTokenResponse(true, TokenData(body.response!!.grantType!!, body.response!!.accessToken!!, body.response!!.refreshToken!!))
                            user.userTokenResponse = userTokenData
                            user.save(PlayKuApplication.pref)

                            val userAPI = UserAPI()
                            userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
                                override fun <T> getResponseBody(
                                    body: T,
                                    isSuccess: Boolean,
                                    err: String
                                ) {
                                    if(isSuccess) {
                                        user.save(PlayKuApplication.pref)
                                        val intent = Intent(applicationContext, MapActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        certifyEmail(user.verifyToken)
                                    }
                                }
                            }).getUserInfo(user.getAccessToken())
                        } else {
                            certifyEmail(body.response!!.authVerifyToken!!)
                        }
                    } else {
                        binding.emailWarnNotEqualTv.visibility = View.VISIBLE
                        binding.emailInputCodeEt.setTextColor(Color.RED)
                        Toast.makeText(applicationContext, "올바른 코드가 아닙니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, errorLog, Toast.LENGTH_SHORT).show()
                }
            }
        }).certifyCode(email, code)
        return true
    }

    private fun startTimer(expiredAt : String) {
        val expiredTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expiredAt).time
        val now = Date(System.currentTimeMillis()).time
        var time : Long = (expiredTime - now) / 10
        Log.d("timer", "startTimer: $time, $expiredAt, $expiredTime, $now")

        certifyTimer = timer(period = 10) {
            if(time < 0) {
                runOnUiThread {
                    showTimeoutDialog()
                }
                this.cancel()
                certifyTimer = null
            }
            time--
            val min = time / 100 / 60
            val sec = (time / 100) % 60
            runOnUiThread {
                binding.emailCertifyTimer.text = String.format("%02d:%02d", min, sec)
            }
        }
    }

    private fun showTimeoutDialog() {
        var contentView: View = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.dialog_certify_timeout, null)
        val slideupPopup = SlideUpDialog.Builder(this)
            .setContentView(contentView)
            .create()
        slideupPopup.setCancelable(false)

        val confirmBtn = slideupPopup.findViewById<Button>(R.id.timeout_confirm_btn)
        confirmBtn.setOnClickListener {
            SoundPlayer(this, R.raw.button_click_sound).play()
            binding.emailRequestCountTv.visibility = View.INVISIBLE
            binding.emailGotoKonkukEmailTv.visibility = View.INVISIBLE
            binding.emailInputCodeCl.visibility = View.INVISIBLE
            binding.emailCertifyBtn.visibility = View.INVISIBLE
            binding.emailInputCodeEt.text.clear()

            slideupPopup.dismissAnim()
        }

        slideupPopup.show()
    }

    private fun certifyEmail(verifyToken : String) {
        Log.d("TAG", "init: certifyEmail")
        certifyTimer?.cancel()
        user.verifyToken = verifyToken
        user.email = email

        val intent = Intent(this, PolicyAgreeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}