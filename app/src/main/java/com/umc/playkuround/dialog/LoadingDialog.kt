package com.umc.playkuround.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog


class LoadingDialog(context : Context) : Dialog(context) {

    private lateinit var progressDialog: AppCompatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.umc.playkuround.R.layout.dialog_loading)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun progressON(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing) {
            return
        }
        if (progressDialog.isShowing) {
            progressDialog = AppCompatDialog(activity)
            progressDialog.setCancelable(false)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(com.umc.playkuround.R.layout.dialog_loading)
            progressDialog.show()
        }
        val img_loading_frame =
            progressDialog.findViewById<View>(com.umc.playkuround.R.id.loading_progress_bar) as ImageView?
        val frameAnimation = img_loading_frame!!.background as AnimationDrawable
        img_loading_frame.post { frameAnimation.start() }
        }

    fun progressOFF() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
    }

