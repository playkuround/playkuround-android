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
import com.umc.playkuround.PlayKuApplication.Companion.pref
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.DuplicateResponse
import com.umc.playkuround.data.User
import com.umc.playkuround.data.UserTokenResponse
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
            nickname()


        }

        binding.nicknameEndBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            savename()
        }

    }


    private fun nickname() {
        val userService = UserService()
        val nickname = binding.nicknameEt.text.toString()
        val text = binding.nicknameEt.text.toString()
        val regex = "^[ㄱ-ㅣ가-힣a-zA-Z]+\$".toRegex()

        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is DuplicateResponse) {
                        if (body.response == true ) {
                            binding.nicknameGetCl.background = ContextCompat.getDrawable(this@NicknameActivity, R.drawable.edit_text_wrong)
                            binding.nicknameErOverlabTv.visibility = View.VISIBLE
                             binding.nicknameEndBtn.isEnabled = false }

                        else if (body.response == false && text.matches(regex) && binding.nicknameEt.length() in 2..8 ) {
                            binding.nicknameGetCl.background = ContextCompat.getDrawable(this@NicknameActivity, R.drawable.edit_text)
                            binding.nicknameEndBtn.isEnabled = true
                            binding.nicknameErOverlabTv.visibility = View.INVISIBLE
                            binding.nicknameErRuleTv.visibility = View.INVISIBLE
                        }
                        else {
                            binding.nicknameGetCl.background = ContextCompat.getDrawable(this@NicknameActivity, R.drawable.edit_text_wrong)
                            binding.nicknameEndBtn.isEnabled = false
                            binding.nicknameErRuleTv.visibility = View.VISIBLE
                            binding.nicknameErOverlabTv.visibility = View.INVISIBLE
                        }

                            Log.d("retrofit", "getResponseBody: " + body.response)
                         }

                } else {
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).isDuplicate(nickname)
    }

    private fun savename() {
        binding.nicknameEndBtn.setOnClickListener {
            val userService = UserService()
            val temp = User("test21@test.com", "test21", "컴퓨터공학부", null)
            userService.setOnResponseListener(object : UserService.OnResponseListener() {
                override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                    if (isSuccess) {
                        if (body is UserTokenResponse)
                            temp.userTokenResponse = body
                        user = temp.copy()
                        user.save(pref)
                        Log.d("userInfo", "onCreate: $user")
                    } else {
                        Log.d("retrofit", "getResponseBody: $err")
                        Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                    }
                }
            }).register(temp)
        }
    }


}