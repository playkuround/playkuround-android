package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.umc.playkuround.R

class CountdownDialog(context : Context) : Dialog(context) {

    interface OnFinishListener {
        fun onFinish()
    }

    private var onFinishListener : OnFinishListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_countdown)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.5f)

        val numberIv = findViewById<ImageView>(R.id.countdown_number_iv)
        Handler(Looper.getMainLooper()).postDelayed({
            numberIv.setImageResource(R.drawable.countdown_2)
            Handler(Looper.getMainLooper()).postDelayed({
                numberIv.setImageResource(R.drawable.countdown_1)
                Handler(Looper.getMainLooper()).postDelayed({
                    this.dismiss()
                    onFinishListener?.onFinish()
                }, 1000)
            }, 1000)
        }, 1000)
    }

    fun setOnFinishListener(listener : OnFinishListener) {
        onFinishListener = listener
    }

}