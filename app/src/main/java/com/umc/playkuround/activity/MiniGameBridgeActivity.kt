package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameBridgeBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.fragment.MiniGameTimerFragment

private const val TIME_LIMIT = 10

class MiniGameBridgeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameBridgeBinding
    private lateinit var timerFragment : MiniGameTimerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameBridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerFragment = supportFragmentManager.findFragmentById(R.id.bridge_timer_fragment) as MiniGameTimerFragment
        timerFragment.setTime(TIME_LIMIT)
        timerFragment.setThemeColor(ActivityCompat.getColor(this, R.color.text_color))
        timerFragment.setOnTimeProgressListener(object : MiniGameTimerFragment.OnTimeProgressListener {
            override fun timeUp() {
                showGameOverDialog()
            }

            override fun timeProgress(leftTime: Int) {

            }
        })

        binding.bridgePauseBtn.setOnClickListener {
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

    private fun showGameOverDialog() {
        val gameOverDialog = GameOverDialog(this@MiniGameBridgeActivity)
        gameOverDialog.setOnDismissListener {
            this@MiniGameBridgeActivity.finish()
        }

        gameOverDialog.setInfo(resources.getString(R.string.bridge_timing),  0, 0, 0)
        gameOverDialog.show()
    }

}