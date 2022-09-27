package com.umc.playkuround.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.DialogPlaceRankBinding

class DialogPlaceRankActivity : AppCompatActivity() {

    lateinit var binding : DialogPlaceRankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPlaceRankBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}