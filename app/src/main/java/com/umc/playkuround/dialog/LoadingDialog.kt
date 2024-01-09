package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.bumptech.glide.Glide
import com.umc.playkuround.R


class LoadingDialog(context : Context) : Dialog(context) {

    private var comment = "로딩중..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0.7f)

        val commentTv = findViewById<TextView>(R.id.loading_comment_tv)
        commentTv.text = comment

        val gifImageView = findViewById<ImageView>(R.id.loading_progress_bar)
        Glide.with(context).asGif().load(R.raw.loading).into(gifImageView)
    }

    fun setComment(comment : String) {
        this.comment = comment
        if(this.isShowing) {
            val commentTv = findViewById<TextView>(R.id.loading_comment_tv)
            commentTv.text = comment
        }
    }

}





