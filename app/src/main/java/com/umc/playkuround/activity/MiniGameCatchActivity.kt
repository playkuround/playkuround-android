package com.umc.playkuround.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameCatchBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.dialog.WaitingDialog
import com.umc.playkuround.custom_view.MiniGameTimerFragment
import com.umc.playkuround.network.AdventureData
import com.umc.playkuround.network.GetBadgeResponse
import com.umc.playkuround.network.HighestScoresResponse
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer
import kotlin.random.Random

private const val WINDOW_MOTION_DELAY = 150L
private const val OPEN_TIME = 1500L
private const val TIME_LIMIT = 60

class MiniGameCatchActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameCatchBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private var score = 0

    private val isOpen = Array(30) { false }
    private val isBlackDuck = Array(30) { false }
    private val isOpenTime = Array(TIME_LIMIT) { false }

    private var highestScore = 0
    private var badges = ArrayList<String>()

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestCatchScore
                    }
                }
            }
        }).getGameScores(PlayKuApplication.user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameCatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHighestScore()

        initOpenTime()
        initWindowClick()

        timerFragment = supportFragmentManager.findFragmentById(R.id.catch_duck_timer_fragment) as MiniGameTimerFragment
        timerFragment.setTime(TIME_LIMIT)
        timerFragment.setThemeColor(ActivityCompat.getColor(this, R.color.text_color))
        timerFragment.setOnTimeProgressListener(object : MiniGameTimerFragment.OnTimeProgressListener {
            override fun timeUp() {
                showGameOverDialog()
            }

            override fun timeProgress(leftTime: Int) {
                if(isOpenTime[leftTime]) {
                    SoundPlayer(applicationContext, R.raw.catch_window).play()
                    if(leftTime < 20) {
                        openWindow(false)
                        openWindow(false)
                        openWindow(false)
                        openWindow(false)
                        openWindow(true)
                        openWindow(true)
                        openWindow(true)
                    } else if(leftTime < 40) {
                        openWindow(false)
                        openWindow(false)
                        openWindow(false)
                        openWindow(true)
                        openWindow(true)
                        if(Random.nextDouble() < 0.5)
                            openWindow(true)
                    } else if(leftTime < 60) {
                        openWindow(false)
                        openWindow(false)
                        openWindow(true)
                        if(Random.nextDouble() < 0.5)
                            openWindow(true)
                    }
                }
            }
        })

        binding.catchDuckPauseBtn.setOnClickListener {
            timerFragment.pause()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    timerFragment.start()
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        val countdownDialog = CountdownDialog(this)
        countdownDialog.setOnFinishListener(object : CountdownDialog.OnFinishListener {
            override fun onFinish() {
                timerFragment.start()
            }
        })
        countdownDialog.show()
    }

    override fun onBackPressed() {
        timerFragment.pause()
        val pauseDialog = PauseDialog(this)
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun resume() {
                timerFragment.start()
            }
            override fun home() {
                finish()
            }
        })
        pauseDialog.show()
    }

    private fun openWindow(isBlack : Boolean) {
        fun getAvailableWindowIndex() : Int {
            while(true) {
                val row = Random.nextInt(0, 4)
                val col = Random.nextInt(1, 5)
                val res = row * 6 + col
                if(!isOpen[res]) return res
            }
        }

        val idx = getAvailableWindowIndex()
        val window = binding.catchDuckWindowsGl.getChildAt(idx) as ImageView
        isOpen[idx] = true
        isBlackDuck[idx] = isBlack

        openMotion(window, isBlackDuck[idx])
        Handler(Looper.getMainLooper()).postDelayed({
            if(isOpen[idx]) closeMotion(window, isBlackDuck[idx], false)
            isOpen[idx] = false
            isBlackDuck[idx] = false
        }, OPEN_TIME + WINDOW_MOTION_DELAY)
    }

    private fun initWindowClick() {
        for(i in 0 until 30) {
            binding.catchDuckWindowsGl.getChildAt(i).setOnClickListener {
                if(isOpen[i]) {
                    if(!isBlackDuck[i]) {
                        SoundPlayer(applicationContext, R.raw.catch_white_duck_clicked).play()
                        score += 1
                        (binding.catchDuckWindowsGl.getChildAt(i) as ImageView).setImageResource(R.drawable.catch_duck_white_catched)
                    } else {
                        SoundPlayer(applicationContext, R.raw.catch_black_duck_clicked).play()
                        score -= 1
                        (binding.catchDuckWindowsGl.getChildAt(i) as ImageView).setImageResource(R.drawable.catch_duck_black_catched)
                    }
                    if(score < 0) score = 0
                    binding.catchDuckScoreTv.text = score.toString()

                    isOpen[i] = false
                    closeMotion(binding.catchDuckWindowsGl.getChildAt(i) as ImageView, isBlackDuck[i], true)
                    isBlackDuck[i] = false
                }
            }
        }
    }

    private fun initOpenTime() {
        for(i in isOpenTime.indices) {
            if(i < 20) {
                if(i % 2 == 0) isOpenTime[i] = true
            } else if(i < 60) {
                if(i % 3 == 1) isOpenTime[i] = true
            }
        }
    }

    private fun openMotion(window : ImageView, isBlack : Boolean) {
        if(isBlack) window.setImageResource(R.drawable.catch_duck_black_half)
        else window.setImageResource(R.drawable.catch_duck_white_half)
        Handler(Looper.getMainLooper()).postDelayed({
            if(isBlack) window.setImageResource(R.drawable.catch_duck_black)
            else window.setImageResource(R.drawable.catch_duck_white)
        }, WINDOW_MOTION_DELAY)
    }

    private fun closeMotion(window : ImageView, isBlack : Boolean, isCatched : Boolean) {
        if(isBlack) {
            if(isCatched) window.setImageResource(R.drawable.catch_duck_black_catched_half)
            else window.setImageResource(R.drawable.catch_duck_black_half)
        } else {
            if(isCatched) window.setImageResource(R.drawable.catch_duck_white_catched_half)
            else window.setImageResource(R.drawable.catch_duck_white_half)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            window.setImageResource(R.drawable.catch_duck_close_window)
        }, WINDOW_MOTION_DELAY)
    }

    private fun showGameOverDialog() {
        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameCatchActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                this@MiniGameCatchActivity.finish()
            }

            gameOverDialog.setInfo(resources.getString(R.string.catch_duck), score, highestScore, userTotalScore + score)
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
        }).sendScore(PlayKuApplication.user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, score, "CATCH"))
    }

}