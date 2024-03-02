package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import com.umc.playkuround.R

class WaitingDialog(context : Context) : Dialog(context) {

    interface OnFinishListener {
        fun onFinish()
    }

    private var onFinishListener : OnFinishListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_waiting)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.5f)

        val layoutParams = window!!.attributes
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = layoutParams

        val comment = findViewById<TextView>(R.id.dialog_waiting_comment_tv)
        Handler(Looper.getMainLooper()).postDelayed({
            comment.text = "2초 후 결과 창으로 이동합니다"
            Handler(Looper.getMainLooper()).postDelayed({
                comment.text = "1초 후 결과 창으로 이동합니다"
                Handler(Looper.getMainLooper()).postDelayed({
                    onFinishListener?.onFinish()
                }, 1000)
            }, 1000)
        }, 1000)
    }

    fun setOnFinishListener(listener : OnFinishListener) {
        onFinishListener = listener
    }

}