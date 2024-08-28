package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameTimerBinding
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
import java.util.*
import kotlin.concurrent.timer

class MiniGameTimerActivity : AppCompatActivity() {

    lateinit var binding : ActivityMinigameTimerBinding

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null

    private var highestScore = 0
    private var badges = ArrayList<String>()

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestTimeScore
                    }
                }
            }
        }).getGameScores(PlayKuApplication.user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHighestScore()

        binding.timerPauseBtn.setOnClickListener {
            timerTask?.cancel()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    if(isRunning) {
                        timerTask = timer(period = 10) {
                            time++
                            val sec = time / 100
                            val milli = time % 100

                            runOnUiThread {
                                binding.timerSec.text = String.format("%02d:", sec)
                                binding.timerMilli.text = String.format("%02d", milli)
                            }
                        }
                    }
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        binding.timerStartBt.setOnClickListener {
            SoundPlayer(this, R.raw.timer_button_click).play()
            if(!isRunning) start() else pause()
        }
        binding.timerStopBt.setOnClickListener{
            SoundPlayer(this, R.raw.timer_button_click).play()
            pause()
            check()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        timerTask?.cancel()
        val pauseDialog = PauseDialog(this)
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun resume() {
                if(isRunning) {
                    timerTask = timer(period = 10) {
                        time++
                        val sec = time / 100
                        val milli = time % 100

                        runOnUiThread {
                            binding.timerSec.text = String.format("%02d:", sec)
                            binding.timerMilli.text = String.format("%02d", milli)
                        }
                    }
                }
            }
            override fun home() {
                finish()
            }
        })
        pauseDialog.show()
    }

    private fun check() {
        if (time in 900..1100) {
            SoundPlayer(this, R.raw.timer_correct).play()
            openResultDialog(true)
        } else {
            SoundPlayer(this, R.raw.timer_wrong).play()
            openResultDialog(false)
        }
    }

    //타이머 시작
    private fun start() {
        isRunning = true
        binding.timerStartBt.visibility = View.INVISIBLE
        binding.timerStopBt.visibility = View.VISIBLE

                timerTask = timer(period = 10) {    // timer() 호출
                    time++    // period=10, 0.01초마다 time를 1씩 증가
                    val sec = time / 100    // time/100, 나눗셈의 몫 (초 부분)
                    val milli = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)

                    // UI 조작을 위한 메서드
                    runOnUiThread {
                        binding.timerSec.text = String.format("%02d:", sec)   // TextView 세팅
                        binding.timerMilli.text = String.format("%02d", milli)    // Textview 세팅
                    }
                }
            }


    //타이머 멈춤
    private fun pause() {
        isRunning = false

        binding.timerStartBt.visibility = View.INVISIBLE
        binding.timerStopBt.visibility = View.INVISIBLE

        timerTask?.cancel() // 안전한 호출(?.)로 timerTask가 null이 아니면 cancel() 호출
    }

    // 결과 나오는 창
    private fun openResultDialog(result : Boolean) {
        if(result) {
            binding.timerSec.setTextColor(ActivityCompat.getColor(this, R.color.green_stroke))
            binding.timerMilli.setTextColor(ActivityCompat.getColor(this, R.color.green_stroke))
            binding.timerResultTv.visibility = View.VISIBLE
            binding.timerSuccessIv.visibility = View.VISIBLE
            binding.timerResultTv.text = "성공하셨습니다. 축하합니다!"
            binding.timerResultTv.setTextColor(ActivityCompat.getColor(this, R.color.green_stroke))

            showGameOverDialog()
        } else {
            binding.timerSec.setTextColor(ActivityCompat.getColor(this, R.color.red))
            binding.timerMilli.setTextColor(ActivityCompat.getColor(this, R.color.red))
            binding.timerResultTv.visibility = View.VISIBLE
            binding.timerFailIv.visibility = View.VISIBLE
            binding.timerResultTv.text = "실패하셨습니다. 다시 시도해보세요!"
            binding.timerResultTv.setTextColor(ActivityCompat.getColor(this, R.color.red))

            showGameOverDialog()
        }
    }

    private fun showGameOverDialog() {
        val score =
            if(time == 1000) 200
            else if(time in 975..1025) 40
            else if(time in 950..1050) 35
            else if(time in 925..1075) 30
            else if(time in 900..1100) 20
            else 0

        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameTimerActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                this@MiniGameTimerActivity.finish()
            }

            gameOverDialog.setInfo(resources.getString(R.string.ku_timer), score, highestScore, userTotalScore + score)
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
        }).sendScore(PlayKuApplication.user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, score, "TIME"))
    }

}
