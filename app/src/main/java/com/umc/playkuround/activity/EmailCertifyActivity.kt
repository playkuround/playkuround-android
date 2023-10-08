package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.EmailCertifyResponse
import com.umc.playkuround.data.EmailResponse
import com.umc.playkuround.data.UserTokenResponse
import com.umc.playkuround.databinding.ActivityEmailCertifyBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.SlideUpDialog
import com.umc.playkuround.service.UserService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            email = binding.emailGetEmailEt.text.toString() + "@konkuk.ac.kr"
            //requestCode(email) // 추후 활성화 시켜야 함

            binding.emailRequestCountTv.visibility = View.INVISIBLE
            binding.emailGotoKonkukEmailTv.visibility = View.INVISIBLE
            binding.emailInputCodeCl.visibility = View.INVISIBLE
            binding.emailCertifyBtn.visibility = View.INVISIBLE
            binding.emailInputCodeEt.text.clear()

            binding.emailRequestCodeBtn.isEnabled = false

            if(certifyTimer != null)
                certifyTimer?.cancel()

            // test code ------------------------------------------------------------
            binding.emailGotoKonkukEmailTv.visibility = View.VISIBLE
            binding.emailInputCodeCl.visibility = View.VISIBLE
            binding.emailCertifyBtn.visibility = View.VISIBLE

            binding.emailRequestCountTv.visibility = View.VISIBLE
            binding.emailRequestCountTv.text = "오늘 인증 요청 횟수가 5회 남았습니다."

            val currentTime = LocalDateTime.now()
            val oneMinuteLater = currentTime.plusMinutes(1)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedTime = oneMinuteLater.format(formatter)
            startTimer(formattedTime)
            // ----------------------------------------------------------------------
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
        }

        // 인증하기 버튼 클릭
        binding.emailCertifyBtn.setOnClickListener {
            certifyCode = binding.emailInputCodeEt.text.toString()
            // isCodeCorrect(email, certifyCode)
            // test code -------------------------------------------------------------------------------
            if(certifyCode != "1234") {
                binding.emailWarnNotEqualTv.visibility = View.VISIBLE
//                binding.emailInputCodeCl.background =
//                    ContextCompat.getDrawable(applicationContext, R.drawable.edit_text_wrong)
                Toast.makeText(applicationContext, "적합한 코드가 아닙니다.", Toast.LENGTH_SHORT).show()
            } else certifyEmail()
            // ------------------------------------------------------------------------------------------
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
        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            @SuppressLint("SetTextI18n")
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
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
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).sendEmail(email)
    }

    private fun isCodeCorrect(email : String, code : String) : Boolean {
        Log.d("certify code", "isCodeCorrect: email : $email, code : $code")
        val loading = LoadingDialog(this)
        loading.show()
        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                loading.dismiss()
                if(isSuccess) {
                    if(body is UserTokenResponse) {
                        if(body.response!!.grantType == null) // if code is correct
                            certifyEmail()
                        else {
                            user.userTokenResponse = body.copy()
                            user.save(PlayKuApplication.pref)

                            val userService2 = UserService()
                            userService2.setOnResponseListener(object : UserService.OnResponseListener() {
                                override fun <T> getResponseBody(
                                    body: T,
                                    isSuccess: Boolean,
                                    err: String
                                ) {
                                    if(isSuccess) {
                                        user.save(PlayKuApplication.pref)
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }).getUserInfo(user.getAccessToken())
                        }
                    } else {
                        binding.emailWarnNotEqualTv.visibility = View.VISIBLE
                        binding.emailInputCodeCl.background = ContextCompat.getDrawable(applicationContext, R.drawable.edit_text_wrong)
                        Toast.makeText(applicationContext, "적합한 코드가 아닙니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
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
            binding.emailRequestCountTv.visibility = View.INVISIBLE
            binding.emailGotoKonkukEmailTv.visibility = View.INVISIBLE
            binding.emailInputCodeCl.visibility = View.INVISIBLE
            binding.emailCertifyBtn.visibility = View.INVISIBLE
            binding.emailInputCodeEt.text.clear()
            //binding.emailGetEmailEt.text.clear()

            slideupPopup.dismissAnim()
        }

        slideupPopup.show()
    }

    private fun certifyEmail() {
        Log.d("TAG", "init: certifyEmail")
        certifyTimer?.cancel()
        /*
        * 이메일이 가입된 이메일일 경우 홈 액티비티로 이동
        * 아닐 경우 이용약관 액티비티로 이동
        */
        user.email = email
        val intent = Intent(this, PolicyAgreeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}