package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.DetailAgree01Binding

class DetailAgree01Activity : AppCompatActivity(){

    lateinit var binding : DetailAgree01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailAgree01Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.agree01XBt.setOnClickListener {
            finish()
        }

    }


}