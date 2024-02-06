package com.umc.playkuround.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameAvoidBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.fragment.MiniGameTimerFragment
import com.umc.playkuround.service.AvoidView

private const val TIME_LIMIT = 60

class MiniGameAvoidActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameAvoidBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private var score = 0
    private var life = 3

    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null

    private val accelerometerEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                val x = event.values[0]
                val y = -event.values[1]
                binding.avoidGameView.updateDuck(x, y)
                binding.avoidGameView.updateObstacles()
                binding.avoidGameView.invalidate()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameAvoidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        timerFragment = supportFragmentManager.findFragmentById(R.id.avoid_timer_fragment) as MiniGameTimerFragment
        timerFragment.setTime(TIME_LIMIT)
        timerFragment.setThemeColor(ActivityCompat.getColor(this, R.color.text_color))
        timerFragment.setOnTimeProgressListener(object : MiniGameTimerFragment.OnTimeProgressListener {
            override fun timeUp() {
                sensorManager.unregisterListener(accelerometerEventListener)
                showGameOverDialog()
            }

            override fun timeProgress(leftTime: Int) {
                var num = 10
                if(leftTime < 20) num = 20
                else if(leftTime < 30) num = 15
                else if(leftTime < 40) num = 10
                else if(leftTime < 50) num = 5
                if(leftTime % 3 == 0)
                    binding.avoidGameView.addGerms(num)
                if(leftTime % 5 == 0)
                    binding.avoidGameView.addBoats(num/4)

                score += if(leftTime < 30) 20
                else if(leftTime < 40) 15
                else if(leftTime < 50) 10
                else 5
                binding.avoidScoreTv.text = score.toString()
            }
        })

        binding.avoidGameView.setOnHitListener(object : AvoidView.OnHitListener {
            override fun hit() {
                life--
                when(life) {
                    2 -> binding.avoidLife1Iv.setImageResource(R.drawable.typing_empty_heart)
                    1 -> binding.avoidLife2Iv.setImageResource(R.drawable.typing_empty_heart)
                    0 -> {
                        binding.avoidLife3Iv.setImageResource(R.drawable.typing_empty_heart)
                        sensorManager.unregisterListener(accelerometerEventListener)
                        timerFragment.pause()
                        showGameOverDialog()
                    }
                }
            }
        })

        binding.avoidPauseBtn.setOnClickListener {
            timerFragment.pause()
            sensorManager.unregisterListener(accelerometerEventListener)
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    accelerometerSensor?.let {
                        sensorManager.registerListener(
                            accelerometerEventListener,
                            it,
                            SensorManager.SENSOR_DELAY_GAME
                        )
                    }
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
                accelerometerSensor?.let {
                    sensorManager.registerListener(
                        accelerometerEventListener,
                        it,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                }
                timerFragment.start()
            }
        })
        countdownDialog.show()
    }

    private fun showGameOverDialog() {
        val gameOverDialog = GameOverDialog(this@MiniGameAvoidActivity)
        gameOverDialog.setOnDismissListener {
            this@MiniGameAvoidActivity.finish()
        }

        gameOverDialog.setInfo(resources.getString(R.string.bridge_timing),  score, 0, 0)
        gameOverDialog.show()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerEventListener)
    }

}