package com.umc.playkuround.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

private const val TIME_LIMIT = 30

class MiniGameBridgeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameBridgeBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private var score = 0

    private var duckThread = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameBridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_bad)
                showResultView()
            }
        })

        binding.bridgeStopBtn.setOnClickListener {
            if(binding.bridgeDuckView.isPause()) return@setOnClickListener

            when(binding.bridgeDuckView.stop()) {
                "perfect" -> {
                    binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_perfect)
                    score += 3
                    binding.bridgeScoreTv.text = score.toString()
                }
                "good" -> {
                    binding.bridgeResultIv.setImageResource(R.drawable.bridge_timing_good)
                    score += 1
                    binding.bridgeScoreTv.text = score.toString()
                }
                "bad" -> {
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

    private fun startDuckThread() {
        var duck = 0
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
        val waitingDialog = WaitingDialog(this)
        waitingDialog.setOnFinishListener(object : WaitingDialog.OnFinishListener {
            override fun onFinish() {
                waitingDialog.dismiss()
                val gameOverDialog = GameOverDialog(this@MiniGameBridgeActivity)
                gameOverDialog.setOnDismissListener {
                    this@MiniGameBridgeActivity.finish()
                }

                gameOverDialog.setInfo(resources.getString(R.string.bridge_timing),  score, 0, userTotalScore + score)
                gameOverDialog.show()
            }
        })
        waitingDialog.show()
    }

}