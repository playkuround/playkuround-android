package com.umc.playkuround.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.umc.playkuround.R
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.CopyOnWriteArrayList

private const val ERROR_RANGE = 16
private const val SPEED = 1

class BridgeDuckView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    interface OnBadListener {
        fun onBad()
    }

    private var onBadListener : OnBadListener? = null

    private var timer : Timer? = null
    private var handler = Handler(Looper.getMainLooper())

    private var isPause = true

    inner class Duck(private val isWhite : Boolean) {
        private var duckImg : Drawable? = null
        var pos = 0

        init {
            duckImg = if(isWhite)
                ContextCompat.getDrawable(context, R.drawable.bridge_timing_white_duck)
            else
                ContextCompat.getDrawable(context, R.drawable.bridge_timing_black_duck)
        }

        fun draw(canvas : Canvas) {
            duckImg?.let {
                if(isWhite) it.setBounds(pos, 0, height + pos, height)
                else it.setBounds(width - height - pos, 0, width - pos, height)
                it.draw(canvas)
            }
        }
    }

    private var whiteDucks = CopyOnWriteArrayList<Duck>()
    private var blackDucks = CopyOnWriteArrayList<Duck>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        blackDucks.forEach {
            it.draw(canvas)
        }
        whiteDucks.forEach {
            it.draw(canvas)
        }
    }

    fun start() {
        isPause = false
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                var deleteDuck : Duck? = null
                blackDucks.forEach {
                    it.pos += SPEED
                    if(it.pos > width/2 - height/2 + ERROR_RANGE)
                        deleteDuck = it
                }
                blackDucks.remove(deleteDuck)

                whiteDucks.forEach {
                    it.pos += SPEED
                    if(it.pos > width/2 - height/2 + ERROR_RANGE)
                        deleteDuck = it
                }
                whiteDucks.remove(deleteDuck)

                handler.post {
                    invalidate()
                    if(deleteDuck != null)
                        onBadListener?.onBad()
                }
            }
        },0, 3)
    }

    fun pause() {
        isPause = true
        timer?.cancel()
    }

    fun addDuck() {
        whiteDucks.add(Duck(true))
        blackDucks.add(Duck(false))
    }

    fun stop() : String {
        var returnVal = "no"
        if(whiteDucks.isNotEmpty() && blackDucks.isNotEmpty()) {
            returnVal = if(width/2 - height/2 - whiteDucks[0].pos < ERROR_RANGE / 2) {
                "perfect"
            } else if(width/2 - height/2 - whiteDucks[0].pos < ERROR_RANGE) {
                "good"
            } else "bad"

            whiteDucks.removeAt(0)
            blackDucks.removeAt(0)
        }
        return returnVal
    }

    fun setOnBadListener(listener: OnBadListener) {
        onBadListener = listener
    }

    fun isPause() : Boolean = isPause

}