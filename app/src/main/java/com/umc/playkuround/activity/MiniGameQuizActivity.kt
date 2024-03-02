package com.umc.playkuround.activity

import android.app.Dialog
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
import com.umc.playkuround.util.PlayKuApplication
import com.umc.playkuround.util.PlayKuApplication.Companion.userTotalScore

class MiniGameQuizActivity : AppCompatActivity() {

    lateinit var binding : ActivityMinigameQuizBinding

    private lateinit var quiz : Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        quiz = Quiz(1, "", ArrayList(), -1)
        //quiz = getQuiz()
        initQuizView()
    }

    private fun getQuiz() : Quiz {
        val landmark = intent.getSerializableExtra("landmark") as LandMark
        return Quiz(landmark.id, "", ArrayList(), -1)
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
            startTimer(15)
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
            showGameOverDialog()
        }
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
        val waitingDialog = WaitingDialog(this)
        waitingDialog.setOnFinishListener(object : WaitingDialog.OnFinishListener {
            override fun onFinish() {
                waitingDialog.dismiss()
                val gameOverDialog = GameOverDialog(this@MiniGameQuizActivity)
                gameOverDialog.setOnDismissListener {
                    this@MiniGameQuizActivity.finish()
                }

                gameOverDialog.setInfo(resources.getString(R.string.ku_quiz), 20, 0, userTotalScore + 20)
                gameOverDialog.show()
            }
        })
        waitingDialog.show()
    }

}