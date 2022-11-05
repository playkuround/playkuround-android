package com.umc.playkuround.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.umc.playkuround.R
import com.umc.playkuround.data.DuplicateResponse
import com.umc.playkuround.databinding.ActivityNicknameBinding
import com.umc.playkuround.fragment.HomeFragment
import com.umc.playkuround.service.UserService
import kotlin.Result.Companion.success


class NicknameActivity : AppCompatActivity() {

    lateinit var binding: ActivityNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nicknameEt.doAfterTextChanged {
            binding.nicknameEndBtn.isEnabled = !it.isNullOrBlank()
            duplicate()
            nickname()


        }

        binding.nicknameEndBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }




    }

    private fun nickname() {
            val text = binding.nicknameEt.text.toString()
            val regex = "^[ㄱ-ㅣ가-힣a-zA-Z]+\$".toRegex()
            isMatch(text, regex)

    }

    private fun duplicate() {
        val userService = UserService()
        val nickname = binding.nicknameEt.text.toString()

        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is DuplicateResponse) {
                        Log.d("retrofit", "getResponseBody: " + body.response)
                         }

                } else {
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).isDuplicate(nickname)
    }

    fun isMatch(text: String, regex: Regex) {


        if (text.matches(regex) && binding.nicknameEt.length() in 2..8 ) {
            //여기서 닉네임 중복 판단하고 아래로 가게 해주면 될듯?
            binding.nicknameGetCl.background = ContextCompat.getDrawable(this, R.drawable.edit_text)
            binding.nicknameEndBtn.isEnabled = true
            binding.nicknameErRuleTv.visibility = View.INVISIBLE
        }
//        else if (body.response == true ) {
//            binding.nicknameGetCl.background = ContextCompat.getDrawable(this@NicknameActivity, R.drawable.edit_text_wrong)
//            binding.nicknameErOverlabTv.visibility = View.VISIBLE
//            binding.nicknameEndBtn.isEnabled = false
//        }

        else {
            binding.nicknameGetCl.background = ContextCompat.getDrawable(this, R.drawable.edit_text_wrong)
            binding.nicknameErRuleTv.visibility = View.VISIBLE
            binding.nicknameEndBtn.isEnabled = false
        }
    }


}