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
import com.umc.playkuround.fragment.MiniGameTimerFragment
import com.umc.playkuround.custom_view.BridgeDuckView

private const val TIME_LIMIT = 60

class MiniGameBridgeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameBridgeBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private var score = 0

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
                showGameOverDialog()
            }

            override fun timeProgress(leftTime: Int) {
                if(leftTime > 1)
                    binding.bridgeDuckView.addDuck()
            }
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
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    timerFragment.start()
                    binding.bridgeDuckView.start()
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
            }
        })
        countdownDialog.show()
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
        val gameOverDialog = GameOverDialog(this@MiniGameBridgeActivity)
        gameOverDialog.setOnDismissListener {
            this@MiniGameBridgeActivity.finish()
        }

        gameOverDialog.setInfo(resources.getString(R.string.bridge_timing),  score * 10, 0, 0)
        gameOverDialog.show()
    }

}