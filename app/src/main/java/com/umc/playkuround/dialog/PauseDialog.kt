package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import com.umc.playkuround.R

class PauseDialog(context : Context) : Dialog(context) {

    interface OnSelectListener {
        fun resume()
        fun home()
    }

    private var onSelectListener : OnSelectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pause)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        val resumeBtn = findViewById<Button>(R.id.pause_dialog_resume_btn)
        val homeBtn = findViewById<Button>(R.id.pause_dialog_home_btn)

        resumeBtn.setOnClickListener {
            onSelectListener?.resume()
            this@PauseDialog.dismiss()
        }
        homeBtn.setOnClickListener {
            onSelectListener?.home()
            this@PauseDialog.dismiss()
        }
    }

    fun setOnSelectListener(listener : OnSelectListener) {
        onSelectListener = listener
    }

}