package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityMajorChoiceBinding

class MajorChoiceActivity : AppCompatActivity() {

    lateinit var binding : ActivityMajorChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}