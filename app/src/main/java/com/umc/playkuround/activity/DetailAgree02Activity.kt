package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.inflate
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.inflate
import com.umc.playkuround.databinding.DetailAgree01Binding
import com.umc.playkuround.databinding.DetailAgree02Binding

class DetailAgree02Activity : AppCompatActivity(){

    lateinit var binding : DetailAgree02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailAgree02Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.agree01XBt.setOnClickListener {
            finish()
        }

    }


}