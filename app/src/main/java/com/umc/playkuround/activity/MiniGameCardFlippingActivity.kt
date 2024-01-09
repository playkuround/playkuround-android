package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityMinigameCardFlippingBinding
import com.umc.playkuround.dialog.CountdownDialog

class MiniGameCardFlippingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMinigameCardFlippingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameCardFlippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countdownDialog = CountdownDialog(this)
        countdownDialog.show()


    }

}