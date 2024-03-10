package com.umc.playkuround.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameBridgeBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.custom_view.MiniGameTimerFragment
import com.umc.playkuround.custom_view.BridgeDuckView
import com.umc.playkuround.dialog.WaitingDialog
import com.umc.playkuround.network.AdventureData
import com.umc.playkuround.network.GetBadgeResponse
import com.umc.playkuround.network.HighestScoresResponse
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

private const val TIME_LIMIT = 30

class MiniGameBridgeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameBridgeBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private var score = 0

    private var duckThread = Timer()

    private var highestScore = 0
    private var badges = ArrayList<String>()

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestHongBridgeScore
                    }
                }
            }
        }).getGameScores(PlayKuApplication.user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameBridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHighestScore()

        timerFragment = supportFragmentManager.findFragmentById(R.id.bridge_timer_fragment) as MiniGameTimerFragment
        timerFragment.setTime(TIME_LIMIT)
        timerFragment.setThemeColor(ActivityCompat.getColor(this, R.color.text_color))
        timerFragment.setOnTimeProgressListener(object : MiniGameTimerFragment.OnTimeProgressListener {
            override fun timeUp() {
                binding.bridgeDuckView.pause()
                duckThread.cancel()
                showGameOverDialog()
            }

            override fun timeProgress(leftTime: Int) {}
        })

        binding.bridgeDuckView.setOnBadListener(object : BridgeDuckView.OnBadListener {
            override fun onBad() {
                SoundPlayer(applicationContext, R.raw.bridge_bad).play()
                binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_bad)
                showResultView()
            }
        })

        binding.bridgeStopBtn.setOnClickListener {
            if(binding.bridgeDuckView.isPause()) return@setOnClickListener

            when(binding.bridgeDuckView.stop()) {
                "perfect" -> {
                    SoundPlayer(applicationContext, R.raw.bridge_good).play()
                    binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_perfect)
                    score += 3
                    binding.bridgeScoreTv.text = score.toString()
                }
                "good" -> {
                    SoundPlayer(applicationContext, R.raw.bridge_good).play()
                    binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_good)
                    score += 1
                    binding.bridgeScoreTv.text = score.toString()
                }
                "bad" -> {
                    SoundPlayer(applicationContext, R.raw.bridge_bad).play()
                    binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_bad)
                }
                else -> return@setOnClickListener
            }

            showResultView()
        }

        binding.bridgePauseBtn.setOnClickListener {
            timerFragment.pause()
            binding.bridgeDuckView.pause()
            duckThread.cancel()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    timerFragment.start()
                    binding.bridgeDuckView.start()
                    startDuckThread()
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
                binding.bridgeDuckView.start()
                startDuckThread()
            }
        })
        countdownDialog.show()
    }

    override fun onBackPressed() {
        timerFragment.pause()
        binding.bridgeDuckView.pause()
        duckThread.cancel()
        val pauseDialog = PauseDialog(this)
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun resume() {
                timerFragment.start()
                binding.bridgeDuckView.start()
                startDuckThread()
            }
            override fun home() {
                finish()
            }
        })
        pauseDialog.show()
    }

    private fun startDuckThread() {
        var duck = 0
        duckThread = Timer()
        duckThread.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val rand = Random.nextDouble()
                if(duck < 4 && rand > 0.35) {
                    duck++
                    binding.bridgeDuckView.addDuck()
                } else if(rand > 0.65) {
                    duck--
                    binding.bridgeDuckView.addDuck()
                }
            }
        },0,500)
    }

    private fun showResultView() {
        val fadeIn = ObjectAnimator.ofFloat(binding.bridgeResultIv, View.ALPHA, 0f, 1f)
        fadeIn.duration = 400
        fadeIn.interpolator = AccelerateDecelerateInterpolator()

        val bounceUp = ObjectAnimator.ofFloat(binding.bridgeResultIv, View.TRANSLATION_Y, 50f, 0f)
        bounceUp.duration = 400
        bounceUp.interpolator = AccelerateDecelerateInterpolator()

        val fadeOut = ObjectAnimator.ofFloat(binding.bridgeResultIv, View.ALPHA, 1f, 0f)
        fadeOut.duration = 400
        fadeOut.interpolator = AccelerateDecelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeIn, bounceUp)

        val sequentialSet = AnimatorSet()
        sequentialSet.playSequentially(animatorSet, fadeOut)

        sequentialSet.start()
    }

    private fun showGameOverDialog() {
        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameBridgeActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                this@MiniGameBridgeActivity.finish()
            }

            gameOverDialog.setInfo(resources.getString(R.string.bridge_timing),  score, highestScore, userTotalScore + score)
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
        }).sendScore(PlayKuApplication.user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, score, "CUPID"))
    }

}