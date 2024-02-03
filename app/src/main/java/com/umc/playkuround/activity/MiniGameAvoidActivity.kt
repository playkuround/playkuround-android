package com.umc.playkuround.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameAvoidBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.fragment.MiniGameTimerFragment

private const val TIME_LIMIT = 30
private const val NS2S = 1.0f/1000000000.0f

class MiniGameAvoidActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameAvoidBinding
    private lateinit var timerFragment : MiniGameTimerFragment
    private var score = 0

    private lateinit var sensorManager: SensorManager
    private var gyroscopeSensor: Sensor? = null

    private var roll : Double = 0.0
    private var pitch : Double = 0.0
    private var yaw : Double = 0.0
    private var timestamp : Double = 0.0
    private var dt : Double = 0.0
    private var rad2dgr = 180 / Math.PI


    private val gyroscopeEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                dt = (event.timestamp - timestamp) * NS2S
                timestamp = event.timestamp.toDouble()

                if(dt - timestamp * NS2S != 0.0) {
                    roll += x * dt
                    pitch += y * dt
                    yaw += z * dt
                }

                Log.d("isoo", "roll: $roll, pitch: $pitch, yaw: $yaw")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameAvoidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        timerFragment = supportFragmentManager.findFragmentById(R.id.avoid_timer_fragment) as MiniGameTimerFragment
        timerFragment.setTime(TIME_LIMIT)
        timerFragment.setThemeColor(ActivityCompat.getColor(this, R.color.text_color))
        timerFragment.setOnTimeProgressListener(object : MiniGameTimerFragment.OnTimeProgressListener {
            override fun timeUp() {
                showGameOverDialog()
            }

            override fun timeProgress(leftTime: Int) {

            }
        })

        binding.avoidPauseBtn.setOnClickListener {
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
        val gameOverDialog = GameOverDialog(this@MiniGameAvoidActivity)
        gameOverDialog.setOnDismissListener {
            this@MiniGameAvoidActivity.finish()
        }

        gameOverDialog.setInfo(resources.getString(R.string.bridge_timing),  score, 0, 0)
        gameOverDialog.show()
    }

    override fun onResume() {
        super.onResume()

        gyroscopeSensor?.let {
            sensorManager.registerListener(
                gyroscopeEventListener,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(gyroscopeEventListener)
    }

}