package com.umc.playkuround.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.data.Quiz
import com.umc.playkuround.databinding.ActivityMinigameQuizBinding
import com.umc.playkuround.dialog.GameOverDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.dialog.WaitingDialog
import com.umc.playkuround.network.AdventureData
import com.umc.playkuround.network.GetBadgeResponse
import com.umc.playkuround.network.HighestScoresResponse
import com.umc.playkuround.network.LandmarkAPI
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.pref
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore
import com.umc.playkuround.util.SoundPlayer
import kotlin.random.Random

class MiniGameQuizActivity : AppCompatActivity() {

    lateinit var binding : ActivityMinigameQuizBinding

    private lateinit var quiz : Quiz

    private var score = 0
    private var highestScore = 0
    private var badges = java.util.ArrayList<String>()

    private fun getHighestScore() {
        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if(isSuccess) {
                    if(body is HighestScoresResponse) {
                        highestScore = body.highestScores.highestQuizScore
                    }
                }
            }
        }).getGameScores(PlayKuApplication.user.getAccessToken())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getHighestScore()

        binding.quizPauseBtn.setOnClickListener {
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    // resume
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        quiz = getQuiz()
        initQuizView()
    }

    override fun onBackPressed() {
        val pauseDialog = PauseDialog(this)
        pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
            override fun resume() {
                // resume
            }
            override fun home() {
                finish()
            }
        })
        pauseDialog.show()
    }

    private fun getQuiz() : Quiz {
        val landmarkId = Random.nextInt(1, 54)
        return Quiz(landmarkId, "", ArrayList(), -1)
    }

    private fun initQuizView() {
        binding.quizQuestionTv.text = quiz.question
        binding.quizOption1Tv.text = quiz.options[0]
        binding.quizOption2Tv.text = quiz.options[1]
        binding.quizOption3Tv.text = quiz.options[2]
        binding.quizOption4Tv.text = quiz.options[3]

        binding.quizOption1Cl.setOnClickListener { choose(0) }
        binding.quizOption2Cl.setOnClickListener { choose(1) }
        binding.quizOption3Cl.setOnClickListener { choose(2) }
        binding.quizOption4Cl.setOnClickListener { choose(3) }
    }

    private fun choose(answer : Int) {
        if (quiz.answer == answer) {
            Log.d("Quiz", "choose: correct")
            setViewByResult(true, answer)
        } else {
            binding.quizOption1Cl.isClickable = false
            binding.quizOption2Cl.isClickable = false
            binding.quizOption3Cl.isClickable = false
            binding.quizOption4Cl.isClickable = false

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding.quizIndex1Iv.alpha = 0.5f
                binding.quizIndex2Iv.alpha = 0.5f
                binding.quizIndex3Iv.alpha = 0.5f
                binding.quizIndex4Iv.alpha = 0.5f

                binding.quizOption1Tv.alpha = 0.5f
                binding.quizOption2Tv.alpha = 0.5f
                binding.quizOption3Tv.alpha = 0.5f
                binding.quizOption4Tv.alpha = 0.5f

                binding.quizOption1Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
                binding.quizOption2Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
                binding.quizOption3Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
                binding.quizOption4Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)

                setViewByResult(false, answer)
            }, 100)
        }
    }

    private fun setViewByResult(result : Boolean, chooseNum : Int) {
        if(!result) {
            SoundPlayer(applicationContext, R.raw.quiz_wrong).play()
            when(chooseNum) {
                0 -> {
                    binding.quizOption1Cl.setBackgroundResource(R.drawable.quiz_option_wrong)
                    binding.quizIndex1Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex1Iv.setTextColor(Color.BLACK)
                    binding.quizIndex1Iv.alpha = 1f
                    binding.quizOption1Tv.alpha = 1f
                    binding.quizOption1Tv.setTextColor(Color.BLACK)
                }
                1 -> {
                    binding.quizOption2Cl.setBackgroundResource(R.drawable.quiz_option_wrong)
                    binding.quizIndex2Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex2Iv.setTextColor(Color.BLACK)
                    binding.quizIndex2Iv.alpha = 1f
                    binding.quizOption2Tv.alpha = 1f
                    binding.quizOption2Tv.setTextColor(Color.BLACK)
                }
                2 -> {
                    binding.quizOption3Cl.setBackgroundResource(R.drawable.quiz_option_wrong)
                    binding.quizIndex3Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex3Iv.setTextColor(Color.BLACK)
                    binding.quizIndex3Iv.alpha = 1f
                    binding.quizOption3Tv.alpha = 1f
                    binding.quizOption3Tv.setTextColor(Color.BLACK)
                }
                3 -> {
                    binding.quizOption4Cl.setBackgroundResource(R.drawable.quiz_option_wrong)
                    binding.quizIndex4Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex4Iv.setTextColor(Color.BLACK)
                    binding.quizIndex4Iv.alpha = 1f
                    binding.quizOption4Tv.alpha = 1f
                    binding.quizOption4Tv.setTextColor(Color.BLACK)
                }
            }
            showGameOverDialog()
        } else {
            when(quiz.answer) {
                0 -> {
                    binding.quizOption1Cl.setBackgroundResource(R.drawable.quiz_option_correct)
                    binding.quizIndex1Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex1Iv.setTextColor(Color.BLACK)
                }
                1 -> {
                    binding.quizOption2Cl.setBackgroundResource(R.drawable.quiz_option_correct)
                    binding.quizIndex2Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex2Iv.setTextColor(Color.BLACK)
                }
                2 -> {
                    binding.quizOption3Cl.setBackgroundResource(R.drawable.quiz_option_correct)
                    binding.quizIndex3Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex3Iv.setTextColor(Color.BLACK)
                }
                3 -> {
                    binding.quizOption4Cl.setBackgroundResource(R.drawable.quiz_option_correct)
                    binding.quizIndex4Iv.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.quizIndex4Iv.setTextColor(Color.BLACK)
                }
            }
            binding.quizCorrectTv.visibility = View.VISIBLE

            val waitingDialog = WaitingDialog(this)
            waitingDialog.setIsResult(false)
            waitingDialog.setOnFinishListener(object : WaitingDialog.OnFinishListener {
                override fun onFinish() {
                    waitingDialog.dismiss()
                    score += 10
                    binding.quizScoreTv.text = "$score 점"
                    quiz = getQuiz()
                    restoreView()
                    initQuizView()
                }
            })
            waitingDialog.show()
        }
    }

    private fun restoreView() {
        binding.quizCorrectTv.visibility = View.INVISIBLE

        binding.quizTimerTv.visibility = View.INVISIBLE
        binding.quizTryAgainTv.visibility = View.INVISIBLE

        binding.quizOption1Cl.isClickable = true
        binding.quizOption2Cl.isClickable = true
        binding.quizOption3Cl.isClickable = true
        binding.quizOption4Cl.isClickable = true

        binding.quizIndex1Iv.backgroundTintList = null
        binding.quizIndex1Iv.setTextColor(Color.WHITE)
        binding.quizIndex2Iv.backgroundTintList = null
        binding.quizIndex2Iv.setTextColor(Color.WHITE)
        binding.quizIndex3Iv.backgroundTintList = null
        binding.quizIndex3Iv.setTextColor(Color.WHITE)
        binding.quizIndex4Iv.backgroundTintList = null
        binding.quizIndex4Iv.setTextColor(Color.WHITE)

        binding.quizIndex1Iv.alpha = 1f
        binding.quizIndex2Iv.alpha = 1f
        binding.quizIndex3Iv.alpha = 1f
        binding.quizIndex4Iv.alpha = 1f

        binding.quizOption1Tv.alpha = 1f
        binding.quizOption2Tv.alpha = 1f
        binding.quizOption3Tv.alpha = 1f
        binding.quizOption4Tv.alpha = 1f

        binding.quizOption1Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
        binding.quizOption2Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
        binding.quizOption3Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
        binding.quizOption4Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)

    }

    private fun startTimer(sec : Int) {
        binding.quizTimerTv.visibility = View.VISIBLE
        binding.quizTryAgainTv.visibility = View.VISIBLE

        class TimerHandler : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if(msg.arg1 < 0) {
                    postDelayed({
                        binding.quizTimerTv.visibility = View.INVISIBLE
                        binding.quizTryAgainTv.visibility = View.INVISIBLE

                        binding.quizOption1Cl.isClickable = true
                        binding.quizOption2Cl.isClickable = true
                        binding.quizOption3Cl.isClickable = true
                        binding.quizOption4Cl.isClickable = true

                        binding.quizIndex1Iv.backgroundTintList = null
                        binding.quizIndex1Iv.setTextColor(Color.WHITE)
                        binding.quizIndex2Iv.backgroundTintList = null
                        binding.quizIndex2Iv.setTextColor(Color.WHITE)
                        binding.quizIndex3Iv.backgroundTintList = null
                        binding.quizIndex3Iv.setTextColor(Color.WHITE)
                        binding.quizIndex4Iv.backgroundTintList = null
                        binding.quizIndex4Iv.setTextColor(Color.WHITE)

                        binding.quizIndex1Iv.alpha = 1f
                        binding.quizIndex2Iv.alpha = 1f
                        binding.quizIndex3Iv.alpha = 1f
                        binding.quizIndex4Iv.alpha = 1f

                        binding.quizOption1Tv.alpha = 1f
                        binding.quizOption2Tv.alpha = 1f
                        binding.quizOption3Tv.alpha = 1f
                        binding.quizOption4Tv.alpha = 1f

                        binding.quizOption1Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
                        binding.quizOption2Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
                        binding.quizOption3Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
                        binding.quizOption4Cl.background = ContextCompat.getDrawable(applicationContext, R.drawable.quiz_option)
                    }, 500)
                    return
                }

                val s : Int = msg.arg1 / 1000
                val ms : Int = (msg.arg1 % 1000) / 10

                val result : String = String.format("%02d.%02d", s, ms)
                binding.quizTimerTv.text = result
            }
        }

        val timerHandler = TimerHandler()

        class TimerThread : Runnable {

            override fun run() {
                var time = sec * 1000
                while(time >= 0) {
                    val msg = Message()
                    msg.arg1 = time
                    timerHandler.sendMessage(msg)

                    Thread.sleep(10)
                    time -= 10
                }

                val msg = Message()
                msg.arg1 = -1
                timerHandler.sendMessage(msg)
            }
        }

        val timerThread = Thread(TimerThread())
        timerThread.start()
    }

    private fun showGameOverDialog() {
        SoundPlayer(applicationContext, R.raw.quiz_correct).play()

        fun showGameOverDialog(result : Int) {
            val gameOverDialog = GameOverDialog(this@MiniGameQuizActivity)
            gameOverDialog.setOnDismissListener {
                val resultIntent = Intent()
                resultIntent.putExtra("isNewLandmark", intent.getBooleanExtra("isNewLandmark", false))
                resultIntent.putExtra("badge", badges)
                setResult(result, resultIntent)
                this@MiniGameQuizActivity.finish()
            }

            gameOverDialog.setInfo(resources.getString(R.string.ku_quiz), score, highestScore, userTotalScore + score)
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
        }).sendScore(PlayKuApplication.user.getAccessToken(), AdventureData(landmarkId, latitude, longitude, score, "QUIZ"))
    }

}