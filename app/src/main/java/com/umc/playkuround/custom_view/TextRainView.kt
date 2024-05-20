package com.umc.playkuround.custom_view

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
import kotlin.random.Random

class TextRainView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    private val textPaint = Paint()
    private val strokePaint = Paint()
    private val redTextPaint = Paint()
    private val greenTextPaint = Paint()

    private var speed = 1000L
    private var time = 0L
    private var timer : Timer? = null
    private var handler = Handler(Looper.getMainLooper())

    private val subjects = arrayOf("대학영어", "대학일본어", "비판적사고와토론", "창조적사고와표현", "인문사회글쓰기",
        "사회봉사", "컴퓨팅적사고", "실전취업특강", "외국인글쓰기", "벤처창업및경영","문예창작", "영어사", "사서강독",
        "논리학", "한국고대사", "지형학", "미디어심리학", "상징과이미지", "환및가군", "현대광학", "생화학", "건축설비",
        "구조역학", "로봇공학", "전자회로", "표면화학", "컴파일러", "금융공학", "품질경영", "면역학", "화장품학", "정당론",
        "중급거시", "정책학개론", "국제재무", "데이터마이닝", "공기업경영론", "중국문화론", "원가회계", "혁신경영", "부동산실습",
        "고체물리", "전산모델링", "인공신경망", "화장품화학", "조직학", "약리학", "효소공학", "의약화학", "유전체학", "가축면역학",
        "농업경제학", "우유과학", "식품학개론", "육수학", "조경구조학", "임상로테이션", "광고디자인", "디자인기획", "테일러링",
        "아트타일", "입체일반", "즉흥연기", "초급일본어", "위상수학", "육상경기", "합창합주", "교수설계", "영어작문", "미디어통역",
        "국제통상정책", "논리와사고", "야외스포츠", "생활건강", "과학사", "요가와명상", "하모니인건국", "교육과인간", "영화영어",
        "말과승마", "철학산책", "명저읽기", "신화와영화", "언어와마음", "기초글쓰기", "재무와회계", "인구의정치학", "과학의원리",
        "지리와이슈", "삶과소통", "미디어영어", "소비와행복", "윤리와삶", "상실과회복", "경제학입문", "서양문명사", "동양의지혜",
        "선거와여론", "성과문학", "정치학입문", "녹색지대", "녹색지대", "녹색지대", "녹색지대")

    private val basicSubjects = arrayOf("대학영어", "대학일본어", "비판적사고와토론", "창조적사고와표현", "인문사회글쓰기",
        "사회봉사", "컴퓨팅적사고", "실전취업특강", "외국인글쓰기", "벤처창업및경영")

    private inner class Text(val text : String) {
        var x = 0f
        var y = 40f

        fun draw(canvas : Canvas) {
            canvas.drawText(text, x, y, strokePaint)
            if(basicSubjects.contains(text))
                canvas.drawText(text, x, y, redTextPaint)
            else if(text == "녹색지대")
                canvas.drawText(text, x, y, greenTextPaint)
            else
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

        redTextPaint.color = Color.RED
        redTextPaint.textSize = 45f
        redTextPaint.typeface = Typeface.createFromAsset(context.assets, "neo_dunggeunmo_regular.ttf")

        greenTextPaint.color = Color.GREEN
        greenTextPaint.textSize = 45f
        greenTextPaint.typeface = Typeface.createFromAsset(context.assets, "neo_dunggeunmo_regular.ttf")

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