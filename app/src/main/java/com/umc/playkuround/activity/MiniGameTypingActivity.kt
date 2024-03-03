package com.umc.playkuround.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameTypingBinding
import com.umc.playkuround.dialog.CountdownDialog
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.custom_view.TextRainView
import com.umc.playkuround.dialog.WaitingDialog
import com.umc.playkuround.network.AdventureData
import com.umc.playkuround.network.GetBadgeResponse
import com.umc.playkuround.network.HighestScoresResponse
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer


class MiniGameTypingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameTypingBinding
    private var life = 3
    private  var score = 0

    private var highestScore = 0
    private var badges = java.util.ArrayList<String>()

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestAllClearScore
                    }
                }
            }
        }).getGameScores(PlayKuApplication.user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameTypingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHighestScore()

        binding.typingTextBox.requestFocus()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.typingTextBox, InputMethodManager.SHOW_IMPLICIT)

        binding.typingPauseBtn.setOnClickListener {
            binding.typingTextRainView.pause()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    binding.typingTextRainView.start()
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        binding.typingTextRainView.setOnTextRainDropListener(object : TextRainView.OnTextRainDropListener {
            override fun drop() {
                SoundPlayer(applicationContext, R.raw.typing_fall).play()
                life--
                when(life) {
                    2 -> binding.typingLife1Iv.setImageResource(R.drawable.typing_empty_heart)
                    1 -> binding.typingLife2Iv.setImageResource(R.drawable.typing_empty_heart)
                    0 -> {
                        binding.typingLife3Iv.setImageResource(R.drawable.typing_empty_heart)
                        binding.typingTextRainView.pause()
                        showGameOverDialog()
                    }
                }
            }
        })

        binding.typingTextBox.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE) {
                if(binding.typingTextRainView.deleteText(binding.typingTextBox.text.toString())) {
                    SoundPlayer(applicationContext, R.raw.typing_correct).play()
                    score += if(binding.typingTextBox.text.length <= 4) 2
                    else if(binding.typingTextBox.text.length <= 8) 4
                    else 6

                    binding.typingScoreTv.text = score.toString()
                }
                binding.typingTextBox.text.clear()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.typingTextRainView.bringToFront()
        binding.typingTextBox.bringToFront()

        val countdownDialog = CountdownDialog(this)
        countdownDialog.setOnFinishListener(object : CountdownDialog.OnFinishListener {
            override fun onFinish() {
                binding.typingTextRainView.start()
            }
        })
        countdownDialog.show()
    }

    private fun showGameOverDialog() {
        SoundPlayer(applicationContext, R.raw.typing_game_over).play()
        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameTypingActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                this@MiniGameTypingActivity.finish()
            }
            gameOverDialog.setInfo(resources.getString(R.string.typing_game), score, highestScore, userTotalScore + score)
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
        }).sendScore(PlayKuApplication.user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, score, "ALL_CLEAR"))
    }

}