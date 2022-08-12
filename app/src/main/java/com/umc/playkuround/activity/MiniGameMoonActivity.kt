package com.umc.playkuround.activity

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityMinigameMoonBinding


class MiniGameMoonActivity : AppCompatActivity() {

    private var count = 100

    lateinit var binding : ActivityMinigameMoonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinigameMoonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moonClickIv.setOnClickListener(View.OnClickListener {
            count--
            binding.moonCountTv.text = count.toString() + ""
            if (count == 0 ) {
                Toast.makeText(this, "맞춤", Toast.LENGTH_SHORT).show()
                binding.moonClickIv.isEnabled = false

            }

        })


    }




}


