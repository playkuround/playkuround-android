package com.umc.playkuround.service

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.core.app.ActivityCompat
import com.umc.playkuround.R
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer
import kotlin.random.Random

class TextRainView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    private val textPaint = Paint()
    private val strokePaint = Paint()

    private var speed = 1000L
    private var time = 0L
    private var timer : Timer? = null
    private var handler = Handler(Looper.getMainLooper())

    private val subjects = arrayOf("대학영어", "대학일본어", "비판적사고와토론", "창조적사고와표현", "인문사회글쓰기",
        "사회봉사", "컴퓨팅적사고", "실전취업특강", "외국인글쓰기", "벤처창업및경영")

//    private val subjects = arrayOf("hello", "world")

    private inner class Text(val text : String) {
        var x = 0f
        var y = 40f

        fun draw(canvas : Canvas) {
            canvas.drawText(text, x, y, strokePaint)
            canvas.drawText(text, x, y, textPaint)
        }

        fun setRandomX() {
            x = Random.Default.nextFloat() * (width.toFloat() - textPaint.measureText(text))
        }
    }

    private val textList = ArrayList<Text>()

    interface OnTextRainDropListener {
        fun drop()
    }

    private var onTextRainDropListener : OnTextRainDropListener? = null

    init {
        textPaint.color = ActivityCompat.getColor(context, R.color.text_color)
        textPaint.textSize = 45f
        textPaint.typeface = Typeface.createFromAsset(context.assets, "neo_dunggeunmo_regular.ttf")

        strokePaint.color = Color.WHITE
        strokePaint.textSize = 45f
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 8f
        strokePaint.typeface = Typeface.createFromAsset(context.assets, "neo_dunggeunmo_regular.ttf")
        textList.add(Text(subjects[Random.nextInt(subjects.size)]))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(timer != null) {
            textList.forEach {
                if (it.x == 0f) it.setRandomX()
                it.draw(canvas)
            }
        }
    }

    fun start() {
        var addTiming = 0L
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if(time >= 30000) {
                    time = 0
                    cancel()
                    if(speed >= 400L) speed -= 200L
                    start()
                    return
                }

                if(addTiming == 0L) {
                    addTiming = time + Random.nextLong(2,7) * speed
                }

                if(time >= addTiming) {
                    textList.add(Text(subjects[Random.nextInt(subjects.size)]))
                    addTiming = 0L
                }

                textList.forEach {
                    it.y += 20f
                }

                val removeList = ArrayList<Text>()
                textList.forEach {
                    if(height != 0 && it.y >= height) {
                        removeList.add(it)
                        handler.post {
                            onTextRainDropListener?.drop()
                        }
                    }
                }
                textList.removeAll(removeList.toSet())

                handler.post {
                    invalidate()
                }
                time += speed
            }
        }, 0, speed)
    }

    fun pause() {
        timer?.cancel()
    }

    fun deleteText(text : String) : Boolean {
        for(i in 0 until textList.size) {
            if(text == textList[i].text) {
                textList.remove(textList[i])
                invalidate()
                return true
            }
        }
        return false
    }

    fun setOnTextRainDropListener(listener : OnTextRainDropListener) {
        onTextRainDropListener = listener
    }

}