package com.umc.playkuround.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameAvoidBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.custom_view.MiniGameTimerFragment
import com.umc.playkuround.custom_view.AvoidView
import com.umc.playkuround.data.User
import com.umc.playkuround.dialog.WaitingDialog
import com.umc.playkuround.network.AdventureData
import com.umc.playkuround.network.GetBadgeResponse
import com.umc.playkuround.network.HighestScoresResponse
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer

private const val TIME_LIMIT = 120

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

    private var highestScore = 0
    private var badges = ArrayList<String>()

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestMicrobeScore
                    }
                }
            }
        }).getGameScores(user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameAvoidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHighestScore()

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

                score += if(leftTime < 30) 4
                else if(leftTime < 40) 3
                else if(leftTime < 50) 2
                else 1
                binding.avoidScoreTv.text = score.toString()
            }
        })

        binding.avoidGameView.setOnHitListener(object : AvoidView.OnHitListener {
            override fun hit() {
                SoundPlayer(applicationContext, R.raw.avoid_hit).play()
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
        SoundPlayer(applicationContext, R.raw.avoid_game_over).play()
        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameAvoidActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                finish()
            }
            gameOverDialog.setInfo(resources.getString(R.string.avoid_obstacle),  score, highestScore, userTotalScore + score)
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
        }).sendScore(user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, score, "SURVIVE"))
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerEventListener)
    }

}