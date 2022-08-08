package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityMinigameTimerBinding

class MiniGameTimerActivity : AppCompatActivity() {

    lateinit var binding : ActivityMinigameTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}