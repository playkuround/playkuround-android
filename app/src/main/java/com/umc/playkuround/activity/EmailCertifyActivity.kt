package com.umc.playkuround.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doAfterTextChanged
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityEmailCertifyBinding

class EmailCertifyActivity : AppCompatActivity() {

    lateinit var binding : ActivityEmailCertifyBinding

    private lateinit var code : String

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
            code = requestCode()

            binding.emailGotoKonkukEmailTv.visibility = View.VISIBLE
            binding.emailInputCodeCl.visibility = View.VISIBLE
            binding.emailCertifyBtn.visibility = View.VISIBLE
            binding.emailResendTv.visibility = View.VISIBLE

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
            if(binding.emailInputCodeEt.text.toString() == code)
                certifyEmail()
            else {
                binding.emailWarnNotEqualTv.visibility = View.VISIBLE
                binding.emailInputCodeCl.background = ContextCompat.getDrawable(this, R.drawable.edit_text_wrong)
            }
        }

        // 재전송 클릭
        binding.emailResendTv.setOnClickListener {
            code = requestCode()
        }
    }

    private fun gotoKonkukEmail() {
        val intentURL = Intent(Intent.ACTION_VIEW, Uri.parse("http://kumail.konkuk.ac.kr/"))
        startActivity(intentURL)
    }

    private fun requestCode() : String {
        // send email to server
        Toast.makeText(this, "요청이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        return "0000"
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