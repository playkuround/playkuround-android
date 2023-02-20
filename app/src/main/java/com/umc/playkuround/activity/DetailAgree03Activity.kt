package com.umc.playkuround.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.DetailAgree01Binding
import com.umc.playkuround.databinding.DetailAgree03Binding

class DetailAgree03Activity : AppCompatActivity(){

    lateinit var binding : DetailAgree03Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailAgree03Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.agree01XBt.setOnClickListener {
            finish()
        }

    }


}