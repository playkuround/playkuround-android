package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.umc.playkuround.R

class ThanksDialog(context : Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_thanks)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.7f)

        val gifImageView = findViewById<ImageView>(R.id.thanks_iv)
        Glide.with(context).asGif().load(R.raw.thanks).into(gifImageView)

        Handler(Looper.getMainLooper()).postDelayed({
            this.dismiss()
        }, 3000)
    }

}