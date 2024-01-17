package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.umc.playkuround.PlayKuApplication
import com.umc.playkuround.R
import com.umc.playkuround.data.LandMark
import com.umc.playkuround.data.Ranking
import com.umc.playkuround.databinding.ActivityMinigameTimerBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.PauseDialog
import com.umc.playkuround.service.UserService
import java.util.*
import kotlin.concurrent.timer

class MiniGameTimerActivity : AppCompatActivity() {

    lateinit var binding : ActivityMinigameTimerBinding

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moonPauseBtn.setOnClickListener {
            timerTask?.cancel()
            val pauseDialog = PauseDialog(this)
            pauseDialog.setOnSelectListener(object : PauseDialog.OnSelectListener {
                override fun resume() {
                    if(isRunning) {
                        timerTask = timer(period = 10) {
                            time++
                            val sec = time / 100
                            val milli = time % 100

                            runOnUiThread {
                                binding.timerSec.text = String.format("%02d:", sec)
                                binding.timerMilli.text = String.format("%02d", milli)
                            }
                        }
                    }
                }
                override fun home() {
                    finish()
                }
            })
            pauseDialog.show()
        }

        binding.timerStartBt.setOnClickListener {
            if(!isRunning) start() else pause()
        }
        binding.timerStopBt.setOnClickListener{
            pause()
            lapTime()
            check()
        }
        binding.timerRestartBt.setOnClickListener {
            reset()
        }
    }

    private fun check() {
        if (time in 990..1010) {
            //Toast.makeText(this, "맞춤", Toast.LENGTH_SHORT).show()
            openResultDialog(true)}

        else {
            //Toast.makeText(this, "틀림", Toast.LENGTH_SHORT).show()
            openResultDialog(false)}
    }

    //타이머 시작
    private fun start() {
        isRunning = true
        binding.timerStartBt.visibility = View.INVISIBLE
        binding.timerStopBt.visibility = View.VISIBLE
        binding.timerRestartBt.visibility = View.INVISIBLE// 시작버튼을 일시정지 이미지로 변경

                timerTask = timer(period = 10) {    // timer() 호출
                    time++    // period=10, 0.01초마다 time를 1씩 증가
                    val sec = time / 100    // time/100, 나눗셈의 몫 (초 부분)
                    val milli = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)

                    // UI 조작을 위한 메서드
                    runOnUiThread {
                        binding.timerSec.text = String.format("%02d:", sec)   // TextView 세팅
                        binding.timerMilli.text = String.format("%02d", milli)    // Textview 세팅
                    }
                }
            }


    //타이머 멈춤
    private fun pause() {
        isRunning = false

        binding.timerStartBt.visibility = View.INVISIBLE
        binding.timerStopBt.visibility = View.INVISIBLE
        binding.timerRestartBt.visibility = View.VISIBLE// 일시정지 아이콘에서 start 아이콘으로 변경

        timerTask?.cancel() // 안전한 호출(?.)로 timerTask가 null이 아니면 cancel() 호출
    }



    //타이머 리셋
    @SuppressLint("SetTextI18n")
    private fun reset() {
        binding.timerResultTv.visibility = View.INVISIBLE
        binding.timerSec.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        binding.timerMilli.setTextColor(ActivityCompat.getColor(this, R.color.text_color))
        binding.timerStartBt.visibility = View.VISIBLE
        binding.timerStopBt.visibility = View.INVISIBLE
        binding.timerRestartBt.visibility = View.INVISIBLE
        timerTask?.cancel()	// timerTask가 null이 아니라면 cancel() 호출

        time = 0		// 시간저장 변수 초기화
        isRunning = false	// 현재 진행중인지 판별하기 위한 Boolean 변수 false 세팅
        binding.timerSec.text = "00:"		// TextView 초기화
        binding.timerMilli.text = "00"

    }

    //  타이머 기록 저장
    @SuppressLint("SetTextI18n")
    private fun lapTime() {
        val lapTime = time		// 함수 호출 시 시간(time) 저장

        // apply() 스코프 함수로, TextView를 생성과 동시에 초기화
        val textView = TextView(this).apply {
            textSize = 20f    // fontSize 20 설정
            text = "${lapTime / 100}.${lapTime % 100}"	// 출력할 시간 설정
        }

    }


    // 결과 나오는 창
    private fun openResultDialog(result : Boolean) {
        if(result) {
            //saveAdventureLog()
            binding.timerSec.setTextColor(ActivityCompat.getColor(this, R.color.green_stroke))
            binding.timerMilli.setTextColor(ActivityCompat.getColor(this, R.color.green_stroke))
            binding.timerResultTv.visibility = View.VISIBLE
            binding.timerRestartBt.visibility = View.INVISIBLE
            binding.timerSuccessIv.visibility = View.VISIBLE
            binding.timerResultTv.text = "성공하셨습니다. 축하합니다!"
            binding.timerResultTv.setTextColor(ActivityCompat.getColor(this, R.color.green_stroke))
        } else {
            binding.timerSec.setTextColor(ActivityCompat.getColor(this, R.color.red))
            binding.timerMilli.setTextColor(ActivityCompat.getColor(this, R.color.red))
            binding.timerResultTv.visibility = View.VISIBLE
            binding.timerResultTv.text = "실패하셨습니다. 다시 시도해보세요!"
            binding.timerResultTv.setTextColor(ActivityCompat.getColor(this, R.color.red))
        }
    }

    private fun saveAdventureLog() {
        val landmark = intent.getSerializableExtra("landmark") as LandMark

        val loading = LoadingDialog(this)
        loading.show()

        val userService = UserService()
        userService.setOnResponseListener(object : UserService.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                if (isSuccess) {
                    val userService2 = UserService()

                    userService2.setOnResponseListener(object : UserService.OnResponseListener() {
                        override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                            if(isSuccess) {
                                loading.dismiss()
                                val intent = Intent(applicationContext, DialogPlaceInfoActivity::class.java)
                                intent.putExtra("landmark", landmark)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }).updateUserScore(PlayKuApplication.user.getAccessToken(), Ranking.scoreType.ADVENTURE)
                } else {
                    loading.dismiss()
                    Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
                }
            }
        }).saveAdventureLog(PlayKuApplication.user.getAccessToken(), landmark)
    }

}
