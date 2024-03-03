package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import com.umc.playkuround.R
import com.umc.playkuround.util.SoundPlayer

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
            SoundPlayer(context, R.raw.button_click_sound).play()
            onSelectListener?.resume()
            this@PauseDialog.dismiss()
        }
        homeBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            onSelectListener?.home()
            this@PauseDialog.dismiss()
        }
    }

    fun setOnSelectListener(listener : OnSelectListener) {
        onSelectListener = listener
    }

}