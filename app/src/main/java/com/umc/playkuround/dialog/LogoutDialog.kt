package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.umc.playkuround.R
import com.umc.playkuround.util.SoundPlayer

class LogoutDialog(context : Context) : Dialog(context) {

    interface OnSelectListener {
        fun yes()
    }

    private var onSelectListener : OnSelectListener? = null
    private lateinit var contextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_logout)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.6f)

        val yesBtn = findViewById<Button>(R.id.logout_dialog_yes_btn)
        val noBtn = findViewById<Button>(R.id.logout_dialog_no_btn)
        contextView = findViewById(R.id.logout_dialog_context)

        yesBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            onSelectListener?.yes()
            this@LogoutDialog.dismiss()
        }
        noBtn.setOnClickListener {
            SoundPlayer(context, R.raw.button_click_sound).play()
            this@LogoutDialog.dismiss()
        }
    }

    fun setOnSelectListener(listener : OnSelectListener) {
        onSelectListener = listener
    }

    fun setContext(str : String) {
        contextView.text = str
    }

}