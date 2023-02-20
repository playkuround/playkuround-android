package com.umc.playkuround.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.umc.playkuround.databinding.DialogConfirmBinding

class ConfirmDialog(context: Context, val text : String) : Dialog(context) {

    private lateinit var binding : DialogConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmQuestion.text = text
        binding.confirmYesBtn.setOnClickListener {
            responseListener?.confirm()
        }
        binding.confirmNoBtn.setOnClickListener {
            responseListener?.refuse()
        }
    }

    interface OnResponseListener {
        fun confirm()
        fun refuse()
    }

    private var responseListener : OnResponseListener? = null

    fun setOnResponseListener(listener : OnResponseListener) {
        responseListener = listener
    }

}