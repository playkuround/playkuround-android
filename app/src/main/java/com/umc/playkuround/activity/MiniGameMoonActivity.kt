package com.umc.playkuround.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameMoonBinding
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.dialog.WaitingDialog
import com.umc.playkuround.network.AdventureData
import com.umc.playkuround.network.GetBadgeResponse
import com.umc.playkuround.network.HighestScoresResponse
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer


class MiniGameMoonActivity : AppCompatActivity() {

    private var count = 100

    lateinit var binding : ActivityMinigameMoonBinding
    private var gifCount = 0

    private var highestScore = 0
    private var badges = ArrayList<String>()

    private lateinit var sound : SoundPlayer

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestMoonScore
                    }
                }
            }
        }).getGameScores(PlayKuApplication.user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameMoonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sound = SoundPlayer(applicationContext, R.raw.moon_clicked)
        getHighestScore()

        binding.moonPauseBtn1.setOnClickListener {
            Log.d("isoo", "home: 0")
            val pauseDialog = PauseDialog(this)
            Log.d("isoo", "home: 1")
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    // resume
                }
                override fun home() {
                    Log.d("isoo", "home: clicked")
                    finish()
                }
            })
            pauseDialog.show()
        }

        binding.moonClickIv.setOnClickListener {
            sound.play()
            count--
            binding.moonCountTv.text = count.toString()
            if (count <= 0) {
                SoundPlayer(applicationContext, R.raw.moon_success).play()
                binding.moonClickIv.isEnabled = false
                binding.moonClickIv.setImageResource(R.drawable.moon_four)
                binding.moonClickIv.layoutParams.height = 800
                binding.moonClickIv.layoutParams.width = 1000
                binding.moonClickIv.requestLayout()

                showGameOverDialog()
            }
            else if (count <= 50) {
                if(gifCount != 0) return@setOnClickListener
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        when(gifCount) {
                            0 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_1)
                            1 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_2)
                            2 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_3)
                            3 -> binding.moonClickIv.setImageResource(R.drawable.moon_three_1)
                        }
                        gifCount++
                        if(gifCount < 4)
                            handler.postDelayed(this, 100)
                        else
                            gifCount = 0

                        if(count <= 0)
                            binding.moonClickIv.setImageResource(R.drawable.moon_four)
                    }
                })
            }
            else if (count <= 80) {
                if(gifCount != 0) return@setOnClickListener
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        when(gifCount) {
                            0 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_1)
                            1 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_2)
                            2 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_3)
                            3 -> binding.moonClickIv.setImageResource(R.drawable.moon_two_1)
                        }
                        gifCount++
                        if(gifCount < 4)
                            handler.postDelayed(this, 100)
                        else
                            gifCount = 0
                    }
                })
            }
            else {
                if(gifCount != 0) return@setOnClickListener
                val handler = Handler(Looper.getMainLooper())
                handler.post(object : Runnable {
                    override fun run() {
                        when(gifCount) {
                            0 -> binding.moonClickIv.setImageResource(R.drawable.moon_1)
                            1 -> binding.moonClickIv.setImageResource(R.drawable.moon_2)
                            2 -> binding.moonClickIv.setImageResource(R.drawable.moon_3)
                            3 -> binding.moonClickIv.setImageResource(R.drawable.moon_1)
                        }
                        gifCount++
                        if(gifCount < 4)
                            handler.postDelayed(this, 100)
                        else
                            gifCount = 0
                    }
                })
            }
        }
    }

    private fun showGameOverDialog() {
        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameMoonActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                this@MiniGameMoonActivity.finish()
            }

            gameOverDialog.setInfo(resources.getString(R.string.ku_moon), 20, highestScore, userTotalScore + 20)
            gameOverDialog.show()
        }

        var flag = false
        var isFailed = false

        val waitingDialog = WaitingDialog(this)
        waitingDialog.setOnFinishListener(object : WaitingDialog.OnFinishListener {
            override fun onFinish() {
                waitingDialog.dismiss()
                if(flag) {
                    if(isFailed) showGameOverDialog(Activity.RESULT_CANCELED)
                    else showGameOverDialog(Activity.RESULT_OK)
                }
            }
        })
        waitingDialog.show()

        val landmarkId = intent.getIntExtra("landmarkId", 0)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        val landmarkAPI = LandmarkAPI()
        landmarkAPI.setOnResponseListener(object : LandmarkAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                if(isSuccess) {
                    if(body is GetBadgeResponse) {
                        body.response.newBadges.forEach {
                            badges.add(it.name)
                        }
                        flag = true
                        if (!waitingDialog.isShowing) {
                            showGameOverDialog(Activity.RESULT_OK)
                        }
                    }
                } else {
                    flag = true
                    isFailed = true
                    if(!waitingDialog.isShowing)
                        showGameOverDialog(Activity.RESULT_CANCELED)
                }
            }
        }).sendScore(PlayKuApplication.user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, 20, "MOON"))
    }

}
