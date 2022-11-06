package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.umc.playkuround.R
import com.umc.playkuround.data.EmailCertifyResponse
import com.umc.playkuround.data.EmailResponse
import com.umc.playkuround.databinding.ActivityEmailCertifyBinding
import com.umc.playkuround.service.UserService
import kotlin.concurrent.timer

class EmailCertifyActivity : AppCompatActivity() {

    lateinit var binding : ActivityEmailCertifyBinding

    private var email : String = ""
    private var certifyCode : String = ""

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
            email = binding.emailGetEmailEt.text.toString()
            requestCode(email)

            binding.emailGotoKonkukEmailTv.visibility = View.VISIBLE
            binding.emailInputCodeCl.visibility = View.VISIBLE
            binding.emailCertifyBtn.visibility = View.VISIBLE

            binding.emailRequestCodeBtn.isEnabled = false
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
            isCodeCorrect(email, certifyCode)
        }
    }

    private fun gotoKonkukEmail() {
        val intentURL = Intent(Intent.ACTION_VIEW, Uri.parse("http://kumail.konkuk.ac.kr/"))
        startActivity(intentURL)
    }

    private fun requestCode(email : String) {
        // send email to server
        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            @SuppressLint("SetTextI18n")
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is EmailResponse) {
                        binding.emailRequestCountTv.visibility = View.VISIBLE
                        binding.emailRequestCountTv.text = "오늘 인증 요청 횟수가 " + body.response!!.sendingCount + "회 남았습니다."
                        startTimer(body.response!!.expireAt)
                    }
                }
            }
        }).sendEmail(email)

        Toast.makeText(this, "요청이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun isCodeCorrect(email : String, code : String) : Boolean {
        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is EmailCertifyResponse) {
                        if(body.response) // if code is correct
                            certifyEmail()
                        else {
                            binding.emailWarnNotEqualTv.visibility = View.VISIBLE
                            binding.emailInputCodeCl.background = ContextCompat.getDrawable(applicationContext, R.drawable.edit_text_wrong)

                            binding.emailRequestCodeBtn.text = resources.getText(R.string.request_code_again)
                            binding.emailRequestCodeBtn.isEnabled = true
                        }
                    }
                }
            }
        }).certifyCode(email, code)
        return true
    }

    private fun startTimer(expiredAt : String) {
        var time = 0
        timer(period = 10) {
            if(time < 0)
                this.cancel()
            time--
            val min = time / 100 / 60
            val sec = (time / 100) % 60
            runOnUiThread {
                binding.emailCertifyTimer.text = String.format("%02d:%02d", min, sec)
            }
        }
    }

    private fun certifyEmail() {
        Log.d("TAG", "init: certifyEmail")
        /*
        * 이메일이 가입된 이메일일 경우 홈 액티비티로 이동
        * 아닐 경우 이용약관 액티비티로 이동
        */
        val intent = Intent(this, PolicyAgreeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}