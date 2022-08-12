package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityEmailCertifyBinding

class EmailCertifyActivity : AppCompatActivity() {

    lateinit var binding : ActivityEmailCertifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailCertifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}