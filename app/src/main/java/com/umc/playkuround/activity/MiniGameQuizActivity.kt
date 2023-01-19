package com.umc.playkuround.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.PlayKuApplication.Companion.user
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.ActivityMinigameQuizBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.service.RankingRVAdapter
import com.umc.playkuround.service.UserService

class MiniGameQuizActivity : AppCompatActivity() {

    lateinit var binding : ActivityMinigameQuizBinding

    private class Quiz (
        var question : String,
        var options : Array<String>,
        var answer : Int,
    )

    private lateinit var quiz : Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.quizBackBtn.setOnClickListener {
            this.finish()
        }

        quiz = getQuiz()
        initQuizView()
    }

    // 현재는 더미 값
    private fun getQuiz() : Quiz {
        val question = "경영관을 사용하지 않는 학과는\n어디일까요?"
        val options = Array(4) { "none" }
        options[0] = "2017년 중앙일보 대학평가 결과 인문계열 5위를 차지했다."
        options[1] = "기술경영학과"
        options[2] = "경제학과"
        options[3] = "경영학과"
        val answer = 3

        return Quiz(question, options, answer)
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
            openResultDialog(true)
        } else {
            binding.quizOption1Cl.isClickable = false
            binding.quizOption2Cl.isClickable = false
            binding.quizOption3Cl.isClickable = false
            binding.quizOption4Cl.isClickable = false

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                openResultDialog(false)

                binding.quizOption1Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
                binding.quizOption2Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
                binding.quizOption3Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
                binding.quizOption4Cl.background = ContextCompat.getDrawable(this, R.drawable.quiz_option_disabled)
            }, 100)
        }
    }

    private fun openResultDialog(result : Boolean) {
        val dialog = Dialog(this)
        var root : View = if(result) {
            dialog.setContentView(R.layout.dialog_quiz_correct)
            dialog.findViewById(R.id.dialog_correct_root)
        } else {
            dialog.setContentView(R.layout.dialog_quiz_wrong)
            dialog.findViewById(R.id.dialog_wrong_root)
        }
        root.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            if(!result)
                startTimer(15)
            else {
                saveAdventureLog()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()
    }

    private fun saveAdventureLog() {
        val landmark = intent.getSerializableExtra("landmark") as LandMark

        val loading = LoadingDialog(this)
        loading.show()

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if (isSuccess) {
                    loading.dismiss()
                    val intent = Intent(applicationContext, DialogPlaceInfoActivity::class.java)
                    intent.putExtra("landmark", landmark)
                    startActivity(intent)
                    finish()
                } else {
                    loading.dismiss()
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).saveAdventureLog(user.getAccessToken(), landmark)
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

}