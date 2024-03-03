package com.umc.playkuround.activity


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.umc.playkuround.R
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.network.DuplicateResponse
import com.umc.playkuround.network.UserTokenResponse
import com.umc.playkuround.databinding.ActivityNicknameBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.SoundPlayer


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
            SoundPlayer(this, R.raw.button_click_sound).play()
            saveName()
        }

    }

    private fun nickname() {
        val userAPI = UserAPI()
        val nickname = binding.nicknameEt.text.toString()
        val text = binding.nicknameEt.text.toString()
        val regex = "^[ㄱ-ㅣ가-힣a-zA-Z]+\$".toRegex()

        if(binding.nicknameEt.length() == 0) {
            binding.nicknameErRuleTv.visibility = View.INVISIBLE
            binding.nicknameErOverlabTv.visibility = View.INVISIBLE
            binding.nicknameEndBtn.isEnabled = false
            return
        }

        if(!(text.matches(regex) && binding.nicknameEt.length() in 2..8)){
            binding.nicknameEt.setTextColor(Color.RED)
            binding.nicknameErRuleTv.visibility = View.VISIBLE
            binding.nicknameErOverlabTv.visibility = View.INVISIBLE
            binding.nicknameEndBtn.isEnabled = false
            return
        }

        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is DuplicateResponse) {
                        if (!body.response) {
                            binding.nicknameEt.setTextColor(Color.RED)
                            binding.nicknameErOverlabTv.visibility = View.VISIBLE
                            binding.nicknameErRuleTv.visibility = View.INVISIBLE
                            binding.nicknameEndBtn.isEnabled = false
                        }
                        else {
                            binding.nicknameEt.setTextColor(Color.BLACK)
                            binding.nicknameEndBtn.isEnabled = true
                            binding.nicknameErOverlabTv.visibility = View.INVISIBLE
                            binding.nicknameErRuleTv.visibility = View.INVISIBLE
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).isAvailable(nickname)
    }

    private fun saveName() {
        val loading = LoadingDialog(this)
        loading.show()
        val userAPI = UserAPI()
        user.nickname = binding.nicknameEt.text.toString()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                loading.dismiss()
                if (isSuccess) {
                    if (body is UserTokenResponse)
                        user.userTokenResponse = body.copy()
                    user.save(pref)

                    val intent = Intent(applicationContext, MapActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).register(user)
    }

}