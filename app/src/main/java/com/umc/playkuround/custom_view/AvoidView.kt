package com.umc.playkuround.custom_view

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.umc.playkuround.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class AvoidView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    abstract inner class Object {
        open var x = 200.0
        open var y = 200.0
        open var speed = 0f
        open var width = 1
        open var height = 1

        open fun draw(canvas : Canvas) {}
        open fun updatePos() {}
    }

    inner class Duck : Object() {
        private var duckImg = ContextCompat.getDrawable(context, R.drawable.avoid_duck_default)
        private var transparentDuckImg = ContextCompat.getDrawable(context, R.drawable.avoid_duck_transparent)
        var isInvincible = false
        private var blink = 0
        private var xVel = 0.0
        private var yVel = 0.0

        init {
            width = dpToPx(33f).toInt()
            height = dpToPx(30f).toInt()
            speed = 0.3f
        }

        override fun draw(canvas : Canvas) {
            if(!isInvincible) {
                duckImg?.let {
                    it.setBounds(
                        (x - width / 2).toInt(), (y - height / 2).toInt(),
                        (x + width / 2).toInt(), (y + height / 2).toInt()
                    )
                    it.draw(canvas)
                }
            } else {
                blink++
                if(blink % 10 < 5) {
                    transparentDuckImg?.let {
                        it.setBounds(
                            (x - width / 2).toInt(), (y - height / 2).toInt(),
                            (x + width / 2).toInt(), (y + height / 2).toInt()
                        )
                        it.draw(canvas)
                    }
                } else {
                    duckImg?.let {
                        it.setBounds(
                            (x - width / 2).toInt(), (y - height / 2).toInt(),
                            (x + width / 2).toInt(), (y + height / 2).toInt()
                        )
                        it.draw(canvas)
                    }
                }
            }
        }

        fun updatePos(x : Float, y : Float) {
            duck.xVel += x * speed
            duck.yVel += y * speed

            val xS = duck.xVel / 2 * speed
            val yS = duck.yVel / 2 * speed

            duck.x -= xS
            duck.y -= yS

            if(this@AvoidView.width - duck.width / 2 < duck.x) duck.x = this@AvoidView.width.toDouble() - duck.width / 2
            else if(duck.width / 2 > duck.x) duck.x = (duck.width / 2).toDouble()
            if(this@AvoidView.height - duck.height / 2 < duck.y) duck.y = this@AvoidView.height.toDouble() - duck.height / 2
            else if(duck.height / 2 > duck.y) duck.y = (duck.height / 2).toDouble()
        }
    }

    inner class Germ : Object() {
        private var germImg = ContextCompat.getDrawable(context, R.drawable.avoid_germ)
        private var angle = 120.0

        init {
            width = dpToPx(13f).toInt()
            height = dpToPx(21.12f).toInt()
            speed = 2.8f
            angle = Random.nextDouble(0.0,360.0)
            val rand = Random.nextInt(2)
            if(angle < 90) {
                if(rand == 0) {
                    x = 0.0 - width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = this@AvoidView.height.toDouble() + height
                }
            } else if(angle < 180) {
                if(rand == 0) {
                    x = 0.0 - width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = 0.0 - height
                }
            } else if(angle < 270) {
                if(rand == 0) {
                    x = this@AvoidView.width.toDouble() + width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = 0.0 - height
                }
            } else {
                if(rand == 0) {
                    x = this@AvoidView.width.toDouble() + width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = this@AvoidView.height.toDouble() + height
                }
            }
        }

        override fun draw(canvas : Canvas) {
            germImg?.let {
                it.setBounds((x - width / 2).toInt(), (y - height / 2).toInt(),
                    (x + width / 2).toInt(), (y + height / 2).toInt()
                )
                it.draw(canvas)
            }
        }

        override fun updatePos() {
            x += sin(Math.toRadians(angle)) * speed
            y += -cos(Math.toRadians(angle)) * speed
        }
    }

    inner class Boat : Object() {
        private var boatImg = ContextCompat.getDrawable(context, R.drawable.avoid_boat)
        var angle = 0.0

        init {
            width = dpToPx(27f).toInt()
            height = dpToPx(82f).toInt()
            speed = 5.6f
            angle = Random.nextDouble(0.0,360.0)
            val rand = Random.nextInt(2)
            if(angle < 90) {
                if(rand == 0) {
                    x = 0.0 - width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = this@AvoidView.height.toDouble() + height
                }
            } else if(angle < 180) {
                if(rand == 0) {
                    x = 0.0 - width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = 0.0 - height
                }
            } else if(angle < 270) {
                if(rand == 0) {
                    x = this@AvoidView.width.toDouble() + width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = 0.0 - height
                }
            } else {
                if(rand == 0) {
                    x = this@AvoidView.width.toDouble() + width
                    y = Random.nextDouble(0.0, this@AvoidView.height.toDouble())
                } else {
                    x = Random.nextDouble(0.0, this@AvoidView.width.toDouble())
                    y = this@AvoidView.height.toDouble() + height
                }
            }
        }

        override fun draw(canvas : Canvas) {
            boatImg?.let {
                it.setBounds((x - width / 2).toInt(), (y - height / 2).toInt(),
                    (x + width / 2).toInt(), (y + height / 2).toInt()
                )
                canvas.save()
                canvas.rotate(angle.toFloat(), x.toFloat(), y.toFloat())
                it.draw(canvas)
                canvas.restore()
            }
        }

        override fun updatePos() {
            x += sin(Math.toRadians(angle)) * speed
            y += -cos(Math.toRadians(angle)) * speed
        }
    }

    private var duck = Duck()
    private var germs = ArrayList<Germ>()
    private var boats = ArrayList<Boat>()

    interface OnHitListener {
        fun hit()
    }

    private var onHitListener : OnHitListener? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        duck.x = (width / 2).toDouble()
        duck.y = (height / 2).toDouble()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        duck.draw(canvas)
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

    fun updateObstacles() {
        val remove = ArrayList<Object>()
        germs.forEach {
            it.updatePos()
            if(it.x - it.width*2 > width || it.y - it.height*2 > height
                || it.x + it.width*2 < 0 || it.y + it.height*2 < 0)
                remove.add(it)
        }
        germs.removeAll(remove.toSet())
        remove.clear()
        boats.forEach {
            it.updatePos()
            if(it.x - it.width*2 > width || it.y - it.height*2 > height
                || it.x + it.width*2 < 0 || it.y + it.height*2 < 0)
                remove.add(it)
        }
        boats.removeAll(remove.toSet())
    }

    fun updateDuck(x : Float, y : Float) {
        duck.updatePos(x, y)
        if(duck.isInvincible) return

        for (germ in germs) {
            val shw = germ.width / 2 + duck.width / 2
            val shh = germ.height / 2 + duck.height / 2
            val dx = abs(germ.x - duck.x)
            val dy = abs(germ.y - duck.y)
            if(dx < shw && dy < shh) {
                onHitListener?.hit()
                duck.isInvincible = true
                Handler(Looper.getMainLooper()).postDelayed({
                    duck.isInvincible = false
                }, 2000)
                germs.remove(germ)
                return
            }
        }

        for (boat in boats) {
            fun isDividePt(x11 : Double, y11 : Double, x12 : Double, y12: Double,
                           x21 : Double, y21 : Double, x22 : Double, y22 : Double) : Boolean {
                val f1 = (x12-x11)*(y21-y11) - (y12-y11)*(x21-x11)
                val f2 = (x12-x11)*(y22-y11) - (y12-y11)*(x22-x11)
                return f1*f2 < 0
            }
            fun isCrossPt(x11 : Double, y11 : Double, x12 : Double, y12: Double,
                          x21 : Double, y21 : Double, x22 : Double, y22 : Double) : Boolean {
                val b1 = isDividePt(x11, y11, x12, y12, x21, y21, x22, y22)
                val b2 = isDividePt(x21, y21, x22, y22, x11, y11, x12, y12)
                return b1 && b2
            }

            val sinX = sin(Math.toRadians(180-boat.angle))
            val cosX = cos(Math.toRadians(180-boat.angle))
            val h = boat.height - 20
            val w = boat.width - 20

            val x1 = boat.x + h/2*sinX - w/2*cosX
            val y1 = boat.y + h/2*cosX + w/2*sinX
            val x2 = boat.x + h/2*sinX + w/2*cosX
            val y2 = boat.y + h/2*cosX - w/2*sinX
            val x3 = boat.x - h/2*sinX + w/2*cosX
            val y3 = boat.y - h/2*cosX - w/2*sinX
            val x4 = boat.x - h/2*sinX - w/2*cosX
            val y4 = boat.y - h/2*cosX + w/2*sinX

            val dx = duck.x - 5
            val dy = duck.y - 5
            val dx1 = dx + duck.width/2
            val dy1 = dy + duck.height/2
            val dx2 = dx + duck.width/2
            val dy2 = dy - duck.height/2
            val dx3 = dx - duck.width/2
            val dy3 = dy - duck.height/2
            val dx4 = dx - duck.width/2
            val dy4 = dy + duck.height/2

            val isOverlap =
                    isCrossPt(x1, y1, x2, y2, dx1, dy1, dx2, dy2) ||
                    isCrossPt(x1, y1, x2, y2, dx2, dy2, dx3, dy3) ||
                    isCrossPt(x1, y1, x2, y2, dx3, dy3, dx4, dy4) ||
                    isCrossPt(x1, y1, x2, y2, dx4, dy4, dx1, dy1) ||
                    isCrossPt(x2, y2, x3, y3, dx1, dy1, dx2, dy2) ||
                    isCrossPt(x2, y2, x3, y3, dx2, dy2, dx3, dy3) ||
                    isCrossPt(x2, y2, x3, y3, dx3, dy3, dx4, dy4) ||
                    isCrossPt(x2, y2, x3, y3, dx4, dy4, dx1, dy1) ||
                    isCrossPt(x3, y3, x4, y4, dx1, dy1, dx2, dy2) ||
                    isCrossPt(x3, y3, x4, y4, dx2, dy2, dx3, dy3) ||
                    isCrossPt(x3, y3, x4, y4, dx3, dy3, dx4, dy4) ||
                    isCrossPt(x3, y3, x4, y4, dx4, dy4, dx1, dy1) ||
                    isCrossPt(x4, y4, x1, y1, dx1, dy1, dx2, dy2) ||
                    isCrossPt(x4, y4, x1, y1, dx2, dy2, dx3, dy3) ||
                    isCrossPt(x4, y4, x1, y1, dx3, dy3, dx4, dy4) ||
                    isCrossPt(x4, y4, x1, y1, dx4, dy4, dx1, dy1)

            if(isOverlap) {
                onHitListener?.hit()
                duck.isInvincible = true
                Handler(Looper.getMainLooper()).postDelayed({
                    duck.isInvincible = false
                }, 2000)
                boats.remove(boat)
                return
            }
        }
    }

    fun addGerms(num : Int) {
        for(i in 1..num) {
            germs.add(Germ())
        }
    }

    fun addBoats(num : Int) {
        for(i in 1..num) {
            boats.add(Boat())
        }
    }

    fun setOnHitListener(listener: OnHitListener) {
        onHitListener = listener
    }

}