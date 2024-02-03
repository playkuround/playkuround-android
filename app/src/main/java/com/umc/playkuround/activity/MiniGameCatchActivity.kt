package com.umc.playkuround.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameCatchBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.fragment.MiniGameTimerFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameCatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    randomOpen()
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

    private fun randomOpen() {
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
        if(Random.nextDouble() < 0.6) isBlackDuck[idx] = true

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
                        score++
                        (binding.catchDuckWindowsGl.getChildAt(i) as ImageView).setImageResource(R.drawable.catch_duck_white_catched)
                    } else {
                        score--
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
            val rand = Random.nextDouble()
            if(i < 20) {
                if(rand < 0.35) isOpenTime[i] = true
            } else if(i < 40) {
                if(rand < 0.2) isOpenTime[i] = true
            } else if(i < 60) {
                if(rand < 0.1) isOpenTime[i] = true
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
        val gameOverDialog = GameOverDialog(this@MiniGameCatchActivity)
        gameOverDialog.setOnDismissListener {
            this@MiniGameCatchActivity.finish()
        }

        gameOverDialog.setInfo(resources.getString(R.string.catch_duck), score * 20, 0, 0)
        gameOverDialog.show()
    }

}