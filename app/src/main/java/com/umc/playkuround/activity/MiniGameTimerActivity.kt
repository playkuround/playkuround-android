package com.umc.playkuround.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.R
import com.umc.playkuround.databinding.ActivityMinigameTimerBinding
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

        binding.timerStartBt.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) start() else pause()
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

    private fun check(){
        if (time>=0) {
            Toast.makeText(this, "맞춤", Toast.LENGTH_SHORT).show()
            openResultDialog(true)}

        else {
            Toast.makeText(this, "틀림", Toast.LENGTH_SHORT).show()
            openResultDialog(false)}


    }



    //타이머 시작
    private fun start() {
        binding.timerStartBt.visibility = View.INVISIBLE
        binding.timerStopBt.visibility = View.VISIBLE
        binding.timerRestartBt.visibility = View.INVISIBLE// 시작버튼을 일시정지 이미지로 변경

                timerTask = timer(period = 10) {    // timer() 호출
                    time++    // period=10, 0.01초마다 time를 1씩 증가
                    val sec = time / 100    // time/100, 나눗셈의 몫 (초 부분)
                    val milli = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)

                    // UI조작을 위한 메서드
                    runOnUiThread {
                        binding.timerSec.text = "$sec:"    // TextView 세팅
                        binding.timerMilli.text = "$milli"    // Textview 세팅
                    }
                }
            }


    //타이머 멈춤
    private fun pause() {
        binding.timerStartBt.visibility = View.INVISIBLE
        binding.timerStopBt.visibility = View.INVISIBLE
        binding.timerRestartBt.visibility = View.VISIBLE// 일시정지 아이콘에서 start아이콘으로 변경

            timerTask?.cancel(); // 안전한 호출(?.)로 timerTask가 null이 아니면 cancel() 호출
        }



    //타이머 리셋
    private fun reset() {
        binding.timerStartBt.visibility = View.VISIBLE
        binding.timerStopBt.visibility = View.INVISIBLE
        binding.timerRestartBt.visibility = View.INVISIBLE
        timerTask?.cancel()	// timerTask가 null이 아니라면 cancel() 호출

        time = 0		// 시간저장 변수 초기화
        isRunning = false	// 현재 진행중인지 판별하기 위한 Boolean변수 false 세팅
        binding.timerSec.text = "00:"		// TextView 초기화
        binding.timerMilli.text = "00"

    }

    //  타이머 기록 저장
    private fun lapTime() {
        val lapTime = time		// 함수 호출 시 시간(time) 저장

        // apply() 스코프 함수로, TextView를 생성과 동시에 초기화
        val textView = TextView(this).apply {
            setTextSize(20f)	// fontSize 20 설정
            text = "${lapTime / 100}.${lapTime % 100}"	// 출력할 시간 설정
        }

    }


    // 결과 나오는 창
    private fun openResultDialog(result : Boolean) {
        if(result) {

        } else {

        }
    }


}
