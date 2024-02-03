package com.umc.playkuround.service

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.umc.playkuround.R
import java.util.Timer
import java.util.concurrent.CopyOnWriteArrayList

class AvoidView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    private var timer : Timer? = null
    private var handler = Handler(Looper.getMainLooper())

    abstract inner class Object {
        open var x = 0
        open var y = 0
        open var speed = 0f
        open var angle = 0f
        open var width = 1
        open var height = 1

        open fun draw(canvas : Canvas) {}
        open fun updatePos() {}
    }

    inner class Duck : Object() {
        private var duckImg = ContextCompat.getDrawable(context, R.drawable.avoid_duck_default)
        private var transparentDuckImg = ContextCompat.getDrawable(context, R.drawable.avoid_duck_transparent)

        init {
            width = dpToPx(33f).toInt()
            height = dpToPx(30f).toInt()
        }

        override fun draw(canvas : Canvas) {
            duckImg?.let {
                it.setBounds(x - width / 2, y - height / 2, x + width / 2, y + height / 2)
                it.draw(canvas)
            }
        }

        fun setStatus(speed : Float, angle : Float) {
            this.speed = speed
            this.angle = angle
        }

        override fun updatePos() {

        }
    }

    inner class Germ : Object() {
        private var germImg = ContextCompat.getDrawable(context, R.drawable.avoid_germ)

        override fun draw(canvas : Canvas) {

        }

        override fun updatePos() {

        }
    }

    inner class Boat : Object() {
        private var boatImg = ContextCompat.getDrawable(context, R.drawable.avoid_boat)

        override fun draw(canvas : Canvas) {

        }

        override fun updatePos() {

        }
    }

    private var duck = Duck()
    private var germs = CopyOnWriteArrayList<Germ>()
    private var boats = CopyOnWriteArrayList<Boat>()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        duck.x = width / 2
        duck.y = height / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        duck.draw(canvas!!)
        germs.forEach {
            it.draw(canvas)
        }
        boats.forEach {
            it.draw(canvas)
        }
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }

}