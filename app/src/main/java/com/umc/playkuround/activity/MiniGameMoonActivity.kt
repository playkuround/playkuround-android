package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.ActivityMinigameMoonBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.service.UserService


class MiniGameMoonActivity : AppCompatActivity() {

    private var count = 100

    lateinit var binding : ActivityMinigameMoonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameMoonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moonClickIv.setOnClickListener {
            count--
            binding.moonCountTv.text = count.toString()
            if (count == 0) {
                //Toast.makeText(this, "맞춤", Toast.LENGTH_SHORT).show()
                binding.moonClickIv.isEnabled = false
                binding.moonClickIv.setImageResource(R.drawable.moon_four)
                binding.moonClickIv.getLayoutParams().height = 800
                binding.moonClickIv.getLayoutParams().width = 1000
                binding.moonClickIv.requestLayout()

                //saveAdventureLog()
            }
            else if (count == 80) {
                binding.moonClickIv.setImageResource(R.drawable.moon_two)
            }
            else if (count == 50) {
                binding.moonClickIv.setImageResource(R.drawable.moon_three)
            }


        }

        binding.moonPauseBtn.setOnClickListener {
            this.finish()
        }
    }

    private fun saveAdventureLog() {
        val landmark = intent.getSerializableExtra("landmark") as LandMark

        val loading = LoadingDialog(this)
        loading.show()

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if (isSuccess) {
                    val userService2 = UserService()

                    userService2.setOnResponseListener(object : UserService.OnResponseListener() {
                        override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                            if(isSuccess) {
                                loading.dismiss()
                                val intent = Intent(applicationContext, DialogPlaceInfoActivity::class.java)
                                intent.putExtra("landmark", landmark)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }).updateUserScore(PlayKuApplication.user.getAccessToken(), Ranking.scoreType.ADVENTURE)
                } else {
                    loading.dismiss()
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).saveAdventureLog(PlayKuApplication.user.getAccessToken(), landmark)
    }

}
